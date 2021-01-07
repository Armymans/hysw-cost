package net.zlw.cloud.progressPayment.service;

import net.zlw.cloud.designProject.model.ProgressPaymentInformation;
import net.zlw.cloud.progressPayment.model.vo.PaymentListVo;
import net.zlw.cloud.progressPayment.model.vo.PaymentVo;

import java.util.List;

public interface ProgressPaymentInformationService {
    void addPayment(PaymentVo paymentVo);

    void editPayment(PaymentVo paymentVo);

    PaymentVo findPayment(String id);

    void deletePayment(String id);

    List<PaymentListVo> paymentList(String id);

}
