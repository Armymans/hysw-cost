package net.zlw.cloud.excel.service.impl;


import net.zlw.cloud.excel.dao.BomTableInfomationDao;
import net.zlw.cloud.excel.service.BomTableInfomationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class BomTableInfomationServiceimpl implements BomTableInfomationService {
    @Resource
    private BomTableInfomationDao bomTableInfomationDao;

}
