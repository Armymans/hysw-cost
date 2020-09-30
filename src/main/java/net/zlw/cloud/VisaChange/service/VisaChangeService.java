package net.zlw.cloud.VisaChange.service;



import com.github.pagehelper.PageInfo;
import net.tec.cloud.common.bean.UserInfo;
import net.zlw.cloud.VisaChange.model.vo.VisaChangeInfoVo;
import net.zlw.cloud.VisaChange.model.vo.VisaChangeVo;
import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;

public interface VisaChangeService {


     public PageInfo<VisaChangeVo> findAllPage(VisaChangeVo visaChangeVO, UserInfo loginUser );


     public void delete(String id);

     VisaChangeInfoVo selectById(String id);

     public void approvalProcess(BatchReviewVo batchReviewVo);

     void addVisChangeVo(VisaChangeInfoVo visaChangeInfoVo,UserInfo loginUser);




}
