package net.zlw.cloud.progressPayment.model.vo;

import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;

/**
 * @Author dell
 * @Date 2020/10/15 16:49
 * @Version 1.0
 */
@Data
public class ProgressPaymentTotalPaymentVo {
    private String id;
    private BigDecimal totalPaymentAmount;
    private BigDecimal cumulativeNumberPayment;
    private String accumulativePaymentProportion;
    private String progressPaymentId;
    private String projectType;
    private String receivingTime;
    private String compileTime;
}
