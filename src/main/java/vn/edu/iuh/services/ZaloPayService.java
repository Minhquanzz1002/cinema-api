package vn.edu.iuh.services;

import vn.edu.iuh.dto.common.zalopay.CallBackRequestDTO;
import vn.edu.iuh.dto.common.zalopay.ZaloPayCallBackResponseDTO;
import vn.edu.iuh.dto.common.zalopay.CreateOrderZaloPayResponseDTO;
import vn.edu.iuh.dto.common.zalopay.req.CreateOrderZaloPayRequestDTO;
import vn.edu.iuh.dto.common.zalopay.res.GetOrderZaloPayResponseDTO;
import vn.edu.iuh.models.Payment;

public interface ZaloPayService {
    CreateOrderZaloPayResponseDTO createOrderTransaction(CreateOrderZaloPayRequestDTO body);

    ZaloPayCallBackResponseDTO processCallback(CallBackRequestDTO callBackRequestDTO);

    GetOrderZaloPayResponseDTO getOrderTransaction(String appTransId);
}
