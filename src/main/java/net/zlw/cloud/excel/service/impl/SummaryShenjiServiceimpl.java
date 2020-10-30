package net.zlw.cloud.excel.service.impl;

import net.zlw.cloud.excel.dao.SummaryShenjiDao;
import net.zlw.cloud.excel.service.SummaryShenjiService;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class SummaryShenjiServiceimpl implements SummaryShenjiService {
    @Resource
    private SummaryShenjiDao summaryShenjiDao;

}
