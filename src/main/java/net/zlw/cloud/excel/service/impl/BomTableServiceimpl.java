package net.zlw.cloud.excel.service.impl;


import net.zlw.cloud.excel.dao.BomTableDao;
import net.zlw.cloud.excel.service.BomTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

@Service
@Transactional
public class BomTableServiceimpl implements BomTableService {
    @Autowired
    private BomTableDao bomTableDao1;
}
