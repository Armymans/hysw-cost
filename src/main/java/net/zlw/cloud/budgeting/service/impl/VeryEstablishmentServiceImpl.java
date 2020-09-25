package net.zlw.cloud.budgeting.service.impl;


import net.zlw.cloud.budgeting.mapper.VeryEstablishmentDao;
import net.zlw.cloud.budgeting.service.VeryEstablishmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 控价编制(VeryEstablishment)表服务实现类
 *
 * @author makejava
 * @since 2020-09-23 16:21:45
 */
@Service
@Transactional
public class VeryEstablishmentServiceImpl implements VeryEstablishmentService {
    @Resource
    private VeryEstablishmentDao veryEstablishmentDao;


}