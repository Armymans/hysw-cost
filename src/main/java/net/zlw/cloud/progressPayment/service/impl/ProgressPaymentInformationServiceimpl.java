package net.zlw.cloud.progressPayment.service.impl;

import net.zlw.cloud.progressPayment.mapper.ProgressPaymentInformationDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class ProgressPaymentInformationServiceimpl {
    @Resource
    private ProgressPaymentInformationDao progressPaymentInformationDao;

}
