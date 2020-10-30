package net.zlw.cloud.snsEmailFile.service;

import net.zlw.cloud.snsEmailFile.mapper.EmOrgMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author Armyman
 * @Description 低代码公司
 * @Date 2020/10/29 16:35
 **/

@Service
@Transactional
public class EmOrgService {

    @Autowired
    private EmOrgMapper emOrgMapper;
}
