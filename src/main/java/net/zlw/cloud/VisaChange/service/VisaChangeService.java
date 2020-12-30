package net.zlw.cloud.VisaChange.service;


import net.tec.cloud.common.bean.UserInfo;
import net.zlw.cloud.VisaChange.model.vo.PageVo;
import net.zlw.cloud.VisaChange.model.vo.VisaChangeListVo;
import net.zlw.cloud.VisaChange.model.vo.VisaChangeStatisticVo;
import net.zlw.cloud.VisaChange.model.vo.VisaChangeVo;
import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;
import net.zlw.cloud.warningDetails.model.MemberManage;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface VisaChangeService {

     List<VisaChangeListVo> findAllVisa(PageVo visaChangeVo);

     void addVisa(VisaChangeVo visaChangeVo, UserInfo loginUser, HttpServletRequest request);

     void updateVisa(VisaChangeVo visaChangeVo, UserInfo loginUser,HttpServletRequest request);

     VisaChangeVo findVisaById(String baseId, String visaNum, UserInfo loginUser);

     void batchReview(BatchReviewVo batchReviewVo, UserInfo loginUser,HttpServletRequest request);

     List<VisaChangeStatisticVo> findAllchangeStatistics(String baseId);

     String showHiddenCard(String id, String baseId);

     void deleteVisa(String baseId,UserInfo loginUser,HttpServletRequest request);

     List<MemberManage> costOfPersonnel();

     void renewFile(String id, String baseId);

    void editOutSourceMoney(String id, String upMoney,String downMoney);

     void visaSuccess(String ids, String id);

     void visaBack(String baseId,String back);
}
