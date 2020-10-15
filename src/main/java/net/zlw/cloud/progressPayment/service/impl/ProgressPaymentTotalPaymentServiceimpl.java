package net.zlw.cloud.progressPayment.service.impl;

import net.zlw.cloud.progressPayment.mapper.ProgressPaymentTotalPaymentDao;
import net.zlw.cloud.progressPayment.model.ProgressPaymentTotalPayment;
import net.zlw.cloud.progressPayment.model.vo.ProgressPaymentTotalPaymentVo;
import net.zlw.cloud.progressPayment.service.ProgressPaymentTotalPaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class ProgressPaymentTotalPaymentServiceimpl implements ProgressPaymentTotalPaymentService {
    @Resource
    private ProgressPaymentTotalPaymentDao progressPaymentTotalPaymentDao;

    public List<ProgressPaymentTotalPaymentVo> findAllProgressPaymentTotalPayment(String id){
        List<ProgressPaymentTotalPaymentVo> byProgressPaymentId = progressPaymentTotalPaymentDao.findByProgressPaymentId(id);
        return byProgressPaymentId;
    }

}
