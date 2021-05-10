package com.yoga.qrCode.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class QrCodeResponse extends Response {
    private String customerId;
    private String batchId;
    private String qrCode;
}
