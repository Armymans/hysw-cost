package net.zlw.cloud.VisaChange.service;



import com.github.pagehelper.PageInfo;
import net.tec.cloud.common.bean.UserInfo;
import net.zlw.cloud.VisaChange.model.VisaChange;
import net.zlw.cloud.VisaChange.model.vo.VisaChangeVO;
import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;

public interface VisaChangeService {


     public PageInfo<VisaChangeVO> findAllPage(VisaChangeVO visaChangeVO, UserInfo loginUser );


     public void delete(String id);

     VisaChange selectById(String id);

     public void approvalProcess(BatchReviewVo batchReviewVo);




}
