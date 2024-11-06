package vn.edu.iuh.controllers.v1;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.config.VnPayConfig;
import vn.edu.iuh.dto.res.PaymentResDTO;
import vn.edu.iuh.dto.res.TransactionStatusDTO;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

@Slf4j
@RestController
@RequestMapping("/v1/payment")
public class PaymentController {

    // Pattern to validate input parameters - chỉ chấp nhận chữ, số và một số ký tự đặc biệt an toàn
    private static final Pattern SAFE_PATTERN = Pattern.compile("^[a-zA-Z0-9\\s_\\-\\.]+$");

    // Danh sách ký tự không an toàn cần loại bỏ
    private static final String UNSAFE_CHARACTERS = "[<>&*%\\\\?:;{}()\\[\\]+=~`'\"]";

    // Utility method to validate and sanitize input with additional security
    private String sanitizeRequestPath(String input) {
        if (input == null) return "";

        // Loại bỏ các ký tự không an toàn
        String sanitized = input.replaceAll(UNSAFE_CHARACTERS, "");

        // Chuẩn hóa khoảng trắng
        sanitized = sanitized.trim().replaceAll("\\s+", " ");

        // Giới hạn độ dài để tránh các cuộc tấn công buffer overflow
        if (sanitized.length() > 255) {
            sanitized = sanitized.substring(0, 255);
        }

        return sanitized;
    }

    // Validate amount to prevent manipulation
    private void validateAmount(long amount) {
        if (amount <= 0 || amount > 1000000000000L) { // Giới hạn số tiền hợp lý
            throw new IllegalArgumentException("Invalid amount");
        }
    }

    // Validate create date format
    private void validateDateFormat(String date) {
        if (!date.matches("\\d{14}")) { // Format: yyyyMMddHHmmss
            throw new IllegalArgumentException("Invalid date format");
        }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleInvalidInput(IllegalArgumentException e) {
        PaymentResDTO errorResponse = new PaymentResDTO();
        errorResponse.setStatus("ERROR");
        errorResponse.setMessage("Invalid input parameters: " + e.getMessage());
        errorResponse.setURL("");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @GetMapping("/create_payment")
    public ResponseEntity<?> createPayment(HttpServletRequest request) throws UnsupportedEncodingException {
        try {
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            String vnp_Version = "2.1.0";
            String vnp_Command = "pay";
            String vnp_OrderInfo = sanitizeRequestPath("Thanh toan don hang");
            String orderType = sanitizeRequestPath("other");
            long amount = 1000000 * 100;

            // Validate amount
            validateAmount(amount);

            // Generate and validate transaction reference
            String vnp_TxnRef = sanitizeRequestPath(VnPayConfig.getRandomNumber(8));
            if (!SAFE_PATTERN.matcher(vnp_TxnRef).matches()) {
                throw new IllegalArgumentException("Invalid transaction reference");
            }

            // Validate IP address
//            String vnp_IpAddr = sanitizeRequestPath(VnPayConfig.getIpAddress(req));
//            if (!vnp_IpAddr.matches("^[0-9\\.\\:]+$")) {
//                throw new IllegalArgumentException("Invalid IP address");
//            }
            String vnp_IpAddr = "127.0.0.1";

            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", vnp_Version);
            vnp_Params.put("vnp_Command", vnp_Command);
            vnp_Params.put("vnp_TmnCode", VnPayConfig.vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(amount));
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_OrderType", orderType);

            // URL encode với charset rõ ràng
            String orderInfo = URLEncoder.encode(vnp_OrderInfo + "_" + vnp_TxnRef, StandardCharsets.UTF_8);
            vnp_Params.put("vnp_OrderInfo", orderInfo);
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
            vnp_Params.put("vnp_Locale", "vn");

//            String returnUrl = URLEncoder.encode(VnPayConfig.vnp_ReturnUrl, StandardCharsets.UTF_8);
            log.info("Base URL: {}", baseUrl);
            String returnUrl = baseUrl + VnPayConfig.vnp_ReturnUrl;
            vnp_Params.put("vnp_ReturnUrl", returnUrl);

            // Date handling in GMT+7
            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnp_CreateDate = formatter.format(cld.getTime());
            validateDateFormat(vnp_CreateDate);
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

            cld.add(Calendar.MINUTE, 15);
            String vnp_ExpireDate = formatter.format(cld.getTime());
            validateDateFormat(vnp_ExpireDate);
            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

            // Build query với mã hóa phù hợp
            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();

            Iterator<String> itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = itr.next();
                String fieldValue = vnp_Params.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    // Validate field value
                    if (!SAFE_PATTERN.matcher(fieldValue).matches() && !fieldValue.equals(orderInfo) && !fieldValue.equals(returnUrl)) {
                        throw new IllegalArgumentException("Invalid field value for: " + fieldName);
                    }

                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));

                    query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));

                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }

            String queryUrl = query.toString();
            String vnp_SecureHash = VnPayConfig.hmacSHA512(VnPayConfig.vnp_HashSecret, hashData.toString());
            queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;

            String paymentUrl = VnPayConfig.vnp_PayUrl + "?" + queryUrl;

            PaymentResDTO paymentResDTO = new PaymentResDTO();
            paymentResDTO.setStatus("OK");
            paymentResDTO.setMessage("Successfully");
            paymentResDTO.setURL(paymentUrl);

            return ResponseEntity.status(HttpStatus.OK).body(paymentResDTO);

        } catch (Exception e) {
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
            log.info("Payment information: amount={}, bankCode={}, order={}, responseCode={}", amount, bankCode, order, responseCode);
            // Validate và sanitize tất cả input
            String sanitizedAmount = sanitizeRequestPath(amount);
            if (!sanitizedAmount.matches("^\\d+$")) {
                throw new IllegalArgumentException("Invalid amount format");
            }

            String sanitizedBankCode = sanitizeRequestPath(bankCode);
            if (!SAFE_PATTERN.matcher(sanitizedBankCode).matches()) {
                throw new IllegalArgumentException("Invalid bank code");
            }

            String sanitizedOrder = sanitizeRequestPath(order);
            String sanitizedResponseCode = sanitizeRequestPath(responseCode);
            if (!sanitizedResponseCode.matches("^\\d{2}$")) {
                throw new IllegalArgumentException("Invalid response code");
            }

            TransactionStatusDTO transactionStatusDTO = new TransactionStatusDTO();

            if (sanitizedResponseCode.equals("00")) {
                transactionStatusDTO.setStatus("OK");
                transactionStatusDTO.setMessage("Successfully");
                transactionStatusDTO.setData("");
            } else {
                transactionStatusDTO.setStatus("NO");
                transactionStatusDTO.setMessage("Failed");
                transactionStatusDTO.setData("");
            }
            return ResponseEntity.status(HttpStatus.OK).body(transactionStatusDTO);

        } catch (IllegalArgumentException e) {
            TransactionStatusDTO errorResponse = new TransactionStatusDTO();
            errorResponse.setStatus("ERROR");
            errorResponse.setMessage(e.getMessage());
            errorResponse.setData("");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}