package com.yoga.qrCode.agent;

import com.yoga.qrCode.model.request.UserRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@Slf4j
public class AgentService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${auth.host}")
    private String authIp;

    @Value("${auth.port}")
    private String authPort;

    public String getEncString(String data, String requestId) {
      log.info("{} Getting Encrypted String..", requestId);
      HttpEntity httpEntity = new HttpEntity(data, loadHttpHeaders(requestId));
      String url = "http://" + authIp + ":" + authPort + "/api/v1.0/crypt/enrcypt";
      Map<String, Object> response = invokePost(url, httpEntity, requestId);
      Map<String, Object> responseStatus = (Map<String, Object>) response.get("response");
      if(StringUtils.equals((CharSequence) responseStatus.get("status"),"200")) {
          return (String) responseStatus.get("data");
      }
      return null;
    }

    public String getDctString(String data, String requestId) {
        log.info("{} Getting Decrypted String..", requestId);
        HttpEntity httpEntity = new HttpEntity(data, loadHttpHeaders(requestId));
        String url = "http://" + authIp + ":" + authPort  + "/api/v1.0/crypt/decrypt";
        Map<String, Object> response = invokePost(url, httpEntity, requestId);
        Map<String, Object> responseStatus = (Map<String, Object>) response.get("response");
        if(StringUtils.equals((CharSequence) responseStatus.get("status"),"200")) {
            return (String) responseStatus.get("data");
        }
        return null;
    }

    public String getJwt(UserRequest userRequest) {
        log.info("{} Getting JWT for customerId : {}", userRequest.getRequestId(),userRequest.getCustomerId());
        HttpEntity httpEntity = new HttpEntity(userRequest, loadHttpHeaders(userRequest.getRequestId()));
        String url = "http://" + authIp + ":" + authPort  + "/api/v2.0/token/generate";
        Map<String, Object> response = invokePost(url, httpEntity, userRequest.getRequestId());
        Map<String, Object> responseStatus = (Map<String, Object>) response.get("response");
        if(StringUtils.equals((CharSequence) responseStatus.get("status"),"200")) {
            return (String) responseStatus.get("jwt");
        }
        return null;
    }

    private HttpHeaders loadHttpHeaders(String requestId) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add("requestId", requestId);
        return httpHeaders;
    }

    private Map<String,Object> invokePost(String url, HttpEntity httpEntity, String requestId) {
        log.info("{} Invoking endpoint : {}", requestId, url);
        ResponseEntity<Map> responseEntity =  restTemplate.postForEntity(url, httpEntity, Map.class);
        return responseEntity.getStatusCode() == HttpStatus.OK ? responseEntity.getBody() : null;
    }

}
