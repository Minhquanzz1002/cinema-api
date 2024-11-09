package vn.edu.iuh.modules.zalopay.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.web.bind.annotation.CrossOrigin;
import vn.edu.iuh.config.ZaloPayProperties;
import vn.edu.iuh.modules.zalopay.utils.HMACUtil;
import vn.edu.iuh.modules.zalopay.utils.ZaloPayUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@CrossOrigin("*")
@RequiredArgsConstructor
public class OrderZaloAPI {
    private static final String ORDER_CREATE_ENDPOINT = "https://sb-openapi.zalopay.vn/v2/create";
    private static final String ORDER_STATUS_ENDPOINT = "https://sb-openapi.zalopay.vn/v2/query";
    private final ZaloPayProperties properties;
    private final ObjectMapper objectMapper;

    public Map<String, Object> createOrder(String appUser, long amount) throws IOException {
        log.info("Create order with appUser: {}, amount: {}, callback: {}", appUser, amount, properties.getCallbackUrl());
        log.info("AppId: {}, Key1: {}", properties.getAppId(), properties.getKey1());

        String appTransId = ZaloPayUtils.getCurrentTimeString("yyMMdd") + "_" + new Date().getTime();
        Map<String, Object> embedData = new HashMap<>() {{
            put("redirecturl", properties.getRedirectUrl() != null ? properties.getRedirectUrl() : "");
            put("merchantinfo", appTransId);
            put("promotioninfo", "{\"vietqr\":true}");
        }};

        Map<String, Object> order = new HashMap<>() {{
            put("app_id", properties.getAppId());
            put("app_trans_id", appTransId);
            put("app_time", System.currentTimeMillis());
            put("app_user", appUser);
            put("amount", amount);
            put("description", "B&Q Cinema - Thanh toan don hang");
            put("bank_code", "zalopayapp");
            put("item", "[]");
            put("embed_data", "{\"promotioninfo\":\"\",\"merchantinfo\":\"embeddata123\"}");
            put("callback_url", properties.getCallbackUrl() != null ? properties.getCallbackUrl() : "");
        }};

        String data = String.format("%s|%s|%s|%s|%s|%s|%s",
                order.get("app_id"),
                order.get("app_trans_id"),
                order.get("app_user"),
                order.get("amount"),
                order.get("app_time"),
                order.get("embed_data"),
                order.get("item")
                );
        order.put("mac", HMACUtil.HMacHexStringEncode(HMACUtil.HMACSHA256, properties.getKey1(), data));

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(ORDER_CREATE_ENDPOINT);

        List<NameValuePair> params = order.entrySet().stream()
                .map(e -> new BasicNameValuePair(e.getKey(), e.getValue().toString()))
                .collect(Collectors.toList());

        post.setEntity(new UrlEncodedFormEntity(params));

        CloseableHttpResponse res = client.execute(post);
        String resultJson = new BufferedReader(new InputStreamReader(res.getEntity().getContent())).lines().collect(Collectors.joining("\n"));
        log.info("Result: {}", resultJson);
        return objectMapper.readValue(resultJson, Map.class);
    }
}
