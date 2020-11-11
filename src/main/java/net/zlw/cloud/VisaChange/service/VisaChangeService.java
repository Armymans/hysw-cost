package net.zlw.cloud.VisaChange.service;


import net.zlw.cloud.VisaChange.model.vo.PageVo;
import net.zlw.cloud.VisaChange.model.vo.VisaChangeListVo;
import net.zlw.cloud.VisaChange.model.vo.VisaChangeVo;

import java.util.List;

public interface VisaChangeService {

     List<VisaChangeListVo> findAllVisa(PageVo visaChangeVo);

     void addVisa(VisaChangeVo visaChangeVo,String id);

     void updateVisa(VisaChangeVo visaChangeVo, String id);

     VisaChangeVo findVisaById(String baseId, String visaNum, String id);
}
