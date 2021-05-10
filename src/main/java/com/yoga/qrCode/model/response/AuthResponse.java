package com.yoga.qrCode.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthResponse extends Response {
    private String customerId;
    private String batchId;
    private String jwt;
}
