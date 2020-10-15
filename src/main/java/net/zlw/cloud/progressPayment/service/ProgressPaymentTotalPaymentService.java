package net.zlw.cloud.progressPayment.service;

import net.zlw.cloud.progressPayment.model.vo.ProgressPaymentTotalPaymentVo;

import java.util.List;

public interface ProgressPaymentTotalPaymentService {

    List<ProgressPaymentTotalPaymentVo> findAllProgressPaymentTotalPayment(String id);
}
