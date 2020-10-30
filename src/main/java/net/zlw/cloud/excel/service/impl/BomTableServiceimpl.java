package net.zlw.cloud.excel.service.impl;

import net.zlw.cloud.excel.dao.BomTableDao;
import net.zlw.cloud.excel.service.BomTableService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class BomTableServiceimpl implements BomTableService {
    @Resource
    private BomTableDao bomTableDao;
}
