package vn.edu.iuh.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.edu.iuh.config.ZaloPayProperties;
import vn.edu.iuh.dto.common.zalopay.CallBackRequestDTO;
import vn.edu.iuh.dto.common.zalopay.ZaloPayCallBackResponseDTO;
import vn.edu.iuh.dto.common.zalopay.CreateOrderZaloPayResponseDTO;
import vn.edu.iuh.dto.common.zalopay.req.CreateOrderZaloPayRequestDTO;
import vn.edu.iuh.dto.common.zalopay.res.GetOrderZaloPayResponseDTO;
import vn.edu.iuh.exceptions.BadRequestException;
import vn.edu.iuh.exceptions.DataNotFoundException;
import vn.edu.iuh.exceptions.InternalServerErrorException;
import vn.edu.iuh.models.Order;
import vn.edu.iuh.models.Payment;
import vn.edu.iuh.models.enums.PaymentMethod;
import vn.edu.iuh.models.enums.PaymentStatus;
import vn.edu.iuh.modules.zalopay.ZaloPay;
import vn.edu.iuh.modules.zalopay.utils.HMACUtil;
import vn.edu.iuh.modules.zalopay.utils.ZaloPayUtils;
import vn.edu.iuh.repositories.OrderRepository;
import vn.edu.iuh.repositories.PaymentRepository;
import vn.edu.iuh.services.ZaloPayService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ZaloPayServiceImpl implements ZaloPayService {
    private final ZaloPay zaloPay;
    private final ObjectMapper objectMapper;
    private final ZaloPayProperties properties;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    @Override
    public CreateOrderZaloPayResponseDTO createOrderTransaction(CreateOrderZaloPayRequestDTO body) {
        Order order = orderRepository.findByIdAndDeleted(body.getOrderId(), false).orElseThrow(() -> new DataNotFoundException("Không tìm thấy đơn hàng"));
        try {
            String appTransId = ZaloPayUtils.getCurrentTimeString("yyMMdd") + "_" + new Date().getTime();
            Map<String, Object> orderResponse = zaloPay.createOrderZaloPay(appTransId, "bqcinema", (long) order.getFinalAmount());
            order.setPaymentMethod(PaymentMethod.ZALOPAY);
            orderRepository.save(order);

            Payment payment = Payment.builder()
                    .code(appTransId)
                    .method(PaymentMethod.ZALOPAY)
                    .status(PaymentStatus.UNPAID)
                    .amount(order.getFinalAmount())
                    .order(order)
                    .detail(objectMapper.writeValueAsString(orderResponse))
                    .build();
            paymentRepository.save(payment);

            if (!orderResponse.get("return_code").equals(1)) {
                throw new InternalServerErrorException(orderResponse.get("return_message").toString());
            }

            return CreateOrderZaloPayResponseDTO.builder()
                    .orderUrl((String) orderResponse.get("order_url"))
                    .transId(appTransId)
                    .qrUrl((String) orderResponse.get("order_url"))
                    .build();
        } catch (IOException e) {
            throw new BadRequestException("Error when create order transaction");
        }
    }

    @Override
    public ZaloPayCallBackResponseDTO processCallback(CallBackRequestDTO body) {
        ZaloPayCallBackResponseDTO response = new ZaloPayCallBackResponseDTO();
        String reqMac = HMACUtil.HMacHexStringEncode(HMACUtil.HMACSHA256, properties.getKey2(), body.getData());
        if (reqMac != null & reqMac.equals(body.getMac())) {
            log.info("Thành công");
            log.info("data: {}", body.getData());
            log.info("mac: {}", body.getMac());
            log.info("reqMac: {}", reqMac);
            response.setReturnCode(1);
            response.setReturnMessage("Thành công");
        } else {
            log.info("Thất bại");
            response.setReturnCode(-1);
            response.setReturnMessage("Thất bại");
        }
        return response;
    }

    @Override
    public GetOrderZaloPayResponseDTO getOrderTransaction(String appTransId) {
        Payment payment = paymentRepository.findByCodeAndDeleted(appTransId, false).orElseThrow(() -> new DataNotFoundException("Không tìm thấy đơn hàng"));
        try {
            Map<String, Object> orderResponse = zaloPay.getOrderZaloPay(appTransId);
            int returnCode = (int) orderResponse.get("return_code");
            String message = (String) orderResponse.get("return_message");
            String zpTransId = orderResponse.get("zp_trans_id").toString();

            GetOrderZaloPayResponseDTO.Status status;
            if (returnCode == 1) {
                status = GetOrderZaloPayResponseDTO.Status.SUCCESS;
                payment.setStatus(PaymentStatus.PAID);
                payment.setTransactionId(zpTransId);
                paymentRepository.save(payment);
            } else if (returnCode == 2) {
                status = GetOrderZaloPayResponseDTO.Status.FAILED;
            } else {
                status = GetOrderZaloPayResponseDTO.Status.PENDING;
            }

            return GetOrderZaloPayResponseDTO.builder()
                    .status(status)
                    .message(message)
                    .zpTransId(orderResponse.get("zp_trans_id").toString())
                    .build();
        } catch (IOException | URISyntaxException e) {
            throw new BadRequestException("Error when get order transaction");
        }
    }


}
