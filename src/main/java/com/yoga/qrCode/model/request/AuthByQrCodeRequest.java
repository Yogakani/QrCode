package com.yoga.qrCode.model.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AuthByQrCodeRequest extends Request {
    private String customerId;
    private String batchId;
    private String qrCode;
}
