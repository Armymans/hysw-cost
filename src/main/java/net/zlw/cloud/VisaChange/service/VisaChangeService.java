package net.zlw.cloud.VisaChange.service;


import net.zlw.cloud.VisaChange.model.vo.PageVo;
import net.zlw.cloud.VisaChange.model.vo.VisaChangeListVo;
import net.zlw.cloud.VisaChange.model.vo.VisaChangeStatisticVo;
import net.zlw.cloud.VisaChange.model.vo.VisaChangeVo;
import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;

import java.util.List;

public interface VisaChangeService {

     List<VisaChangeListVo> findAllVisa(PageVo visaChangeVo);

     void addVisa(VisaChangeVo visaChangeVo,String id);

     void updateVisa(VisaChangeVo visaChangeVo, String id);

     VisaChangeVo findVisaById(String baseId, String visaNum, String id);

     void batchReview(BatchReviewVo batchReviewVo, String id);

     List<VisaChangeStatisticVo> findAllchangeStatistics(String baseId);

     String showHiddenCard(String id, String baseId);

     void deleteVisa(String baseId);
}
