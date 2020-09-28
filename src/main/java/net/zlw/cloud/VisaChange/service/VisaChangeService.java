package net.zlw.cloud.VisaChange.service;



import net.tec.cloud.common.bean.UserInfo;
import net.zlw.cloud.VisaApplyChangeInformation.model.VisaChangeInformation;
import net.zlw.cloud.VisaChange.model.VisaChange;
import net.zlw.cloud.VisaChange.model.vo.VisaChangeVO;

import java.util.List;

public interface VisaChangeService {


     public List<VisaChangeVO> findAllPage(VisaChangeVO visaChangeVO, UserInfo loginUser );


     public void delete(String id);




}
