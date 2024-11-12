package vn.edu.iuh.controllers.v1;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.config.VnPayConfig;
import vn.edu.iuh.dto.res.PaymentResDTO;
import vn.edu.iuh.dto.res.TransactionStatusDTO;
import vn.edu.iuh.models.Order;
import vn.edu.iuh.repositories.OrderRepository;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

@Slf4j
@RestController
@RequestMapping("/v1/payment")
public class PaymentController {

    @Autowired
    private OrderRepository orderRepository;

    private static final Pattern SAFE_PATTERN = Pattern.compile("^[a-zA-Z0-9\\s_\\-\\.]+$");
    private static final String UNSAFE_CHARACTERS = "[<>&*%\\\\?:;{}()\\[\\]+=~`'\"]";

    private String sanitizeInput(String input) {
        if (input == null) return "";
        String sanitized = input.replaceAll(UNSAFE_CHARACTERS, "");
        sanitized = sanitized.trim().replaceAll("\\s+", " ");
        return sanitized.length() > 255 ? sanitized.substring(0, 255) : sanitized;
    }

    @GetMapping("/create_payment/{orderId}")
    public ResponseEntity<?> createPayment(@PathVariable UUID orderId, HttpServletRequest request) {
        try {
            // 1. Validate orderId
            if (orderId == null) {
                throw new IllegalArgumentException("Invalid order ID");
            }

            // 2. Lấy đơn hàng theo orderId
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

            // 3. Kiểm tra trạng thái đơn hàng (nếu cần)
            // TODO: Thêm logic kiểm tra trạng thái đơn hàng nếu cần
            // if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            //     throw new IllegalStateException("Order is not in valid state for payment");
            // }

            // 4. Lấy final_amount và chuyển đổi sang VND cents
            long amount = (long) (order.getFinalAmount() * 100);
            if (amount <= 0 || amount > 1000000000000L) {
                throw new IllegalArgumentException("Invalid amount");
            }

            // 5. Khởi tạo thông tin thanh toán
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", "2.1.0");
            vnp_Params.put("vnp_Command", "pay");
            vnp_Params.put("vnp_TmnCode", VnPayConfig.vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(amount));
            vnp_Params.put("vnp_CurrCode", "VND");

            // 6. Tạo mã giao dịch với orderId
            String vnp_TxnRef = orderId.toString() + "_" + VnPayConfig.getRandomNumber(4);
            String vnp_OrderInfo = "Thanh toan don hang " + orderId;
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", URLEncoder.encode(vnp_OrderInfo, StandardCharsets.UTF_8));
            vnp_Params.put("vnp_OrderType", "other");
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_ReturnUrl", baseUrl + VnPayConfig.vnp_ReturnUrl);
            vnp_Params.put("vnp_IpAddr", request.getRemoteAddr());

            // 7. Xử lý thời gian
            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnp_CreateDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

            cld.add(Calendar.MINUTE, 15);
            String vnp_ExpireDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

            // 8. Tạo chuỗi hash data
            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();

            Iterator<String> itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = itr.next();
                String fieldValue = vnp_Params.get(fieldName);
                if (fieldValue != null && !fieldValue.isEmpty()) {
                    hashData.append(fieldName).append('=')
                            .append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8))
                            .append('=')
                            .append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));

                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }

            // 9. Tạo secure hash và URL thanh toán
            String queryUrl = query.toString();
            String vnp_SecureHash = VnPayConfig.hmacSHA512(VnPayConfig.vnp_HashSecret, hashData.toString());
            queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
            String paymentUrl = VnPayConfig.vnp_PayUrl + "?" + queryUrl;

            // 10. Log transaction
            log.info("Creating payment for Order ID: {}, Amount: {}, TxnRef: {}",
                    orderId, amount, vnp_TxnRef);

            // 11. Trả về response
            PaymentResDTO response = new PaymentResDTO();
            response.setStatus("OK");
            response.setMessage("Successfully created payment for Order: " + orderId);
            response.setURL(paymentUrl);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error creating payment for Order ID: " + orderId, e);
            PaymentResDTO errorResponse = new PaymentResDTO();
            errorResponse.setStatus("ERROR");
            errorResponse.setMessage("Payment creation failed: " + e.getMessage());
            errorResponse.setURL("");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/payment_infor")
    public ResponseEntity<?> transaction(
            @RequestParam(value = "vnp_Amount") String amount,
            @RequestParam(value = "vnp_BankCode") String bankCode,
            @RequestParam(value = "vnp_OrderInfo") String order,
            @RequestParam(value = "vnp_ResponseCode") String responseCode
    ) {
        try {
            log.info("Payment information received - Amount: {}, BankCode: {}, Order: {}, ResponseCode: {}",
                    amount, bankCode, order, responseCode);

            // 1. Validate và sanitize input
            String sanitizedAmount = sanitizeInput(amount);
            if (!sanitizedAmount.matches("^\\d+$")) {
                throw new IllegalArgumentException("Invalid amount format");
            }

            String sanitizedBankCode = sanitizeInput(bankCode);
            if (!SAFE_PATTERN.matcher(sanitizedBankCode).matches()) {
                throw new IllegalArgumentException("Invalid bank code");
            }

            String sanitizedOrder = sanitizeInput(order);
            String sanitizedResponseCode = sanitizeInput(responseCode);
            if (!sanitizedResponseCode.matches("^\\d{2}$")) {
                throw new IllegalArgumentException("Invalid response code");
            }

            // 2. Xử lý response từ VNPay
            TransactionStatusDTO transactionStatusDTO = new TransactionStatusDTO();

            // 3. Kiểm tra mã response (00 là thành công)
            if (sanitizedResponseCode.equals("00")) {
                // Extract orderId from vnp_OrderInfo (format: "Thanh toan don hang {orderId}")
                String orderIdStr = sanitizedOrder.replaceAll(".*\\s([a-fA-F0-9-]+)$", "$1");
                UUID orderId = UUID.fromString(orderIdStr);

                // TODO: Cập nhật trạng thái đơn hàng
                // Order order = orderRepository.findById(orderId)
                //         .orElseThrow(() -> new IllegalArgumentException("Order not found"));
                // order.setStatus(OrderStatus.PAID);
                // orderRepository.save(order);

                transactionStatusDTO.setStatus("OK");
                transactionStatusDTO.setMessage("Payment successfully processed");
                transactionStatusDTO.setData(String.format("OrderId: %s, Amount: %s VND, Bank: %s",
                        orderId,
                        Long.parseLong(sanitizedAmount) / 100,
                        sanitizedBankCode));
            } else {
                transactionStatusDTO.setStatus("NO");
                transactionStatusDTO.setMessage("Payment failed");
                transactionStatusDTO.setData(String.format("Error code: %s", sanitizedResponseCode));
            }

            return ResponseEntity.ok(transactionStatusDTO);

        } catch (Exception e) {
            log.error("Error processing payment information", e);
            TransactionStatusDTO errorResponse = new TransactionStatusDTO();
            errorResponse.setStatus("ERROR");
            errorResponse.setMessage("Payment processing failed: " + e.getMessage());
            errorResponse.setData("");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}