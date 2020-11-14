package net.zlw.cloud.VisaChange.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.zlw.cloud.VisaChange.model.VisaApplyChangeInformation;
import net.zlw.cloud.VisaChange.model.VisaChange;
import net.zlw.cloud.progressPayment.model.AuditInfo;

import java.util.List;
import java.util.Objects;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisaChangeVo {


    private VisaChange visaChangeUp;
    private VisaChange visaChangeDown;

    private VisaApplyChangeInformation visaApplyChangeInformationUp;
    private VisaApplyChangeInformation visaApplyChangeInformationDown;

    private String auditNumber;
    private String auditId;

    private String visaNum;
    private String baseId;



}
