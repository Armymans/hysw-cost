package net.zlw.cloud.budgeting.service.impl;

import net.zlw.cloud.budgeting.mapper.CostPreparationDao;
import net.zlw.cloud.budgeting.service.CostPreparationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class CostPreparationServiceImpl implements CostPreparationService {
    @Resource
    private CostPreparationDao costPreparationDao;

}
