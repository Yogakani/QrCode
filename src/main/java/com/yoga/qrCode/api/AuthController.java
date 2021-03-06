package com.yoga.qrCode.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoga.qrCode.model.request.AuthByQrCodeRequest;
import com.yoga.qrCode.model.request.UserRequest;
import com.yoga.qrCode.model.response.AuthResponse;
import com.yoga.qrCode.model.response.Response;
import com.yoga.qrCode.service.UserService;
import com.yoga.qrCode.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

import static com.yoga.qrCode.utils.CommonUtils.getHttpStatusCode;

@RestController
@RequestMapping(value = "/api/v1.0/login")
@Slf4j
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping(value = "/authenticate")
    public ResponseEntity<Response> authenticateUser(@RequestBody UserRequest userRequest, @RequestHeader("requestId") String requestId) {
        log.info("{} Authenticating user : {}", requestId, userRequest.getCustomerId());
        Map<String, Boolean> res = Optional.ofNullable(userRequest)
                .filter(req -> StringUtils.isNotEmpty(req.getCustomerId())  && StringUtils.isNotEmpty(req.getBatchId()))
                .map(req -> userService.authenticate(req, requestId))
                .orElse(null);
        Boolean status = res.get("status") != null ? res.get("status") : Boolean.FALSE;
        log.info("{} Authenticating user status is : {}", requestId, status);
        return new ResponseEntity<>(CommonUtils.prepareUserCheckResponse(res), getHttpStatusCode(status));
    }

    @PostMapping(value = "/authByPassword")
    public ResponseEntity<AuthResponse> authenticateByPassword(@RequestBody UserRequest userRequest, @RequestHeader("requestId") String requestId) {
        log.info("{} Authenticate By Password for user : {}", requestId, userRequest.getCustomerId());
        userRequest.setRequestId(requestId);
        boolean status = Optional.ofNullable(userRequest)
                .filter(req -> StringUtils.isNotEmpty(req.getCustomerId())  && StringUtils.isNotEmpty(req.getBatchId()))
                .map(req -> userService.validatePassword(req))
                .orElse(Boolean.FALSE);
        log.info("{} Authenticating by password status is : {}", requestId, status);
        String jwt = null;
        if (status) {
            jwt = Optional.ofNullable(userService.generateToken(userRequest)).get().orElse(null);
        }
        return new ResponseEntity<>(CommonUtils.prepreAuthResponse(userRequest,jwt,status), getHttpStatusCode(status));
    }

    @PostMapping(value = "/authByQrCode")
    public ResponseEntity<Response> authenticateByQrCode(@RequestBody AuthByQrCodeRequest authReq, @RequestHeader("requestId") String requestId) throws JsonProcessingException {
        log.info("{} Authenticate By QrCode for user : {}", requestId, authReq.getCustomerId());
        authReq.setRequestId(requestId);
        boolean status = Optional.ofNullable(authReq)
                .filter(req -> StringUtils.isNotEmpty(req.getCustomerId())  && StringUtils.isNotEmpty(req.getBatchId()))
                .map(req -> userService.validateQrCode(req))
                .orElse(Boolean.FALSE);
        log.info("{} Authenticating by QrCode status is : {}", requestId, status);
        String jwt = Optional.ofNullable(userService.generateToken(new UserRequest().setCustomerId(authReq.getCustomerId()).setBatchId(authReq.getBatchId())))
                             .get()
                             .orElse(null);
        String msg = objectMapper.writeValueAsString(new AuthResponse().setCustomerId(authReq.getCustomerId())
                .setBatchId(authReq.getBatchId()).setJwt(jwt).setStatus(status));
        return new ResponseEntity<>(new Response().setStatus(status), getHttpStatusCode(status));
    }

}