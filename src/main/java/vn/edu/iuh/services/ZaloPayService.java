package vn.edu.iuh.services;

import vn.edu.iuh.dto.common.zalopay.CallBackRequestDTO;
import vn.edu.iuh.dto.common.zalopay.ZaloPayCallBackResponseDTO;
import vn.edu.iuh.dto.common.zalopay.CreateOrderZaloPayResponseDTO;
import vn.edu.iuh.dto.common.zalopay.req.CreateOrderZaloPayRequestDTO;

public interface ZaloPayService {
    CreateOrderZaloPayResponseDTO createOrderTransaction(CreateOrderZaloPayRequestDTO body);
    ZaloPayCallBackResponseDTO processCallback(CallBackRequestDTO callBackRequestDTO);
}
