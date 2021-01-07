package net.zlw.cloud.progressPayment.model.vo;

import lombok.Data;
import net.zlw.cloud.progressPayment.model.ProgressPaymentInformation;

@Data
public class PaymentVo {

    private ProgressPaymentInformation paymentInfo;
    private String baseId;
}
