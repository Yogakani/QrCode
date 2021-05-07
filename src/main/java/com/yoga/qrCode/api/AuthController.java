package com.yoga.qrCode.api;

import com.yoga.qrCode.model.request.UserRequest;
import com.yoga.qrCode.model.response.Response;
import com.yoga.qrCode.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.yoga.qrCode.utils.CommonUtils.getHttpStatusCode;

@RestController
@RequestMapping(value = "/api/v1.0/login")
@Slf4j
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/authenticate")
    public ResponseEntity<Response> authenticateUser(@RequestBody UserRequest userRequest, @RequestHeader("requestId") String requestId) {
        log.info("{} Authenticating user : {}", requestId, userRequest.getCustomerId());
        boolean status = Optional.ofNullable(userRequest)
                .filter(req -> StringUtils.isNotEmpty(req.getCustomerId())  && StringUtils.isNotEmpty(req.getBatchId()))
                .map(req -> userService.authenticate(req, requestId))
                .orElse(Boolean.FALSE);
        log.info("{} Authenticating user status is : {}", requestId, status);
        return new ResponseEntity<>(new Response().setStatus(status), getHttpStatusCode(status));
    }

    @PostMapping(value = "/authByPassword")
    public ResponseEntity<Response> authenticateByPassword(@RequestBody UserRequest userRequest, @RequestHeader("requestId") String requestId) {
        log.info("{} Authenticate By Password for user : {}", requestId, userRequest.getCustomerId());
        userRequest.setRequestId(requestId);
        boolean status = Optional.ofNullable(userRequest)
                .filter(req -> StringUtils.isNotEmpty(req.getCustomerId())  && StringUtils.isNotEmpty(req.getBatchId()))
                .map(req -> userService.validatePassword(req))
                .orElse(Boolean.FALSE);
        log.info("{} Authenticating by password status is : {}", requestId, status);
        return new ResponseEntity<>(new Response().setStatus(status), getHttpStatusCode(status));
    }

}