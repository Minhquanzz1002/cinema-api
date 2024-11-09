package vn.edu.iuh.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.edu.iuh.config.ZaloPayProperties;
import vn.edu.iuh.dto.common.zalopay.CallBackRequestDTO;
import vn.edu.iuh.dto.common.zalopay.ZaloPayCallBackResponseDTO;
import vn.edu.iuh.dto.common.zalopay.CreateOrderZaloPayResponseDTO;
import vn.edu.iuh.dto.common.zalopay.req.CreateOrderZaloPayRequestDTO;
import vn.edu.iuh.exceptions.BadRequestException;
import vn.edu.iuh.exceptions.DataNotFoundException;
import vn.edu.iuh.models.Order;
import vn.edu.iuh.modules.zalopay.ZaloPay;
import vn.edu.iuh.modules.zalopay.utils.HMACUtil;
import vn.edu.iuh.repositories.OrderRepository;
import vn.edu.iuh.services.ZaloPayService;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ZaloPayServiceImpl implements ZaloPayService {
    private final ZaloPay zaloPay;
    private final ObjectMapper objectMapper;
    private final ZaloPayProperties properties;
    private final OrderRepository orderRepository;

    @Override
    public CreateOrderZaloPayResponseDTO createOrderTransaction(CreateOrderZaloPayRequestDTO body) {
        Order order = orderRepository.findByIdAndDeleted(body.getOrderId(), false).orElseThrow(() -> new DataNotFoundException("Không tìm thấy đơn hàng"));
        try {
            Map<String, Object> orderResponse = zaloPay.createOrderZaloPay("bqcinema", (long) order.getFinalAmount());
            return objectMapper.convertValue(orderResponse, CreateOrderZaloPayResponseDTO.class);
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
            response.setReturnCode(1);
            response.setReturnMessage("Thành công");
        } else {
            log.info("Thất bại");
            response.setReturnCode(-1);
            response.setReturnMessage("Thất bại");
        }
        return response;
    }


}
