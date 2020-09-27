package net.zlw.cloud.VisaChange.service.impl;

import antlr.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import net.tec.cloud.common.bean.UserInfo;
import net.zlw.cloud.VisaChange.mapper.VisaChangeMapper;
import net.zlw.cloud.VisaChange.model.VisaChange;
import net.zlw.cloud.VisaChange.model.vo.VisaChangeVO;
import net.zlw.cloud.VisaChange.service.VisaChangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.util.List;

/***
 * 签证变更逻辑层
 */
@Service
@Transactional
public class VisaChangeImpl implements VisaChangeService {



    @Autowired
    private VisaChangeMapper vcMapper;


    /***
     * 分页查询所有
     */
    @Override
    public List<VisaChangeVO> findAllPage(VisaChangeVO visaChangeVO, UserInfo loginUser ) {
        PageHelper.startPage(visaChangeVO.getPageNum(),visaChangeVO.getPageSize());
        List<VisaChangeVO> all = vcMapper.findAll(visaChangeVO);
        return all;
    }

    @Override
    public void delete(String id) {
        vcMapper.deleteById(id);
    }










}
