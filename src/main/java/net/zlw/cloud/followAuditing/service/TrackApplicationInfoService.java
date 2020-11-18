package net.zlw.cloud.followAuditing.service;


import com.github.pagehelper.PageInfo;
import net.tec.cloud.common.bean.UserInfo;
import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;
import net.zlw.cloud.followAuditing.model.TrackMonthly;
import net.zlw.cloud.followAuditing.model.vo.AuditInfoVo;
import net.zlw.cloud.followAuditing.model.vo.PageVo;
import net.zlw.cloud.followAuditing.model.vo.ReturnTrackVo;
import net.zlw.cloud.followAuditing.model.vo.TrackVo;
import net.zlw.cloud.progressPayment.model.AuditInfo;

import java.util.List;

public interface TrackApplicationInfoService {
    PageInfo<ReturnTrackVo> selectTrackList(PageVo pageVo);


    void deleteById(String id);

    void batchReview(BatchReviewVo id);

    void addTrack(TrackVo trackVo, UserInfo userInfo, String baseId);

    TrackVo selectTrackById(String id);

    List<AuditInfoVo> findAllAuditInfosByTrackId(String id);

    void updateMonthly(TrackMonthly trackMonthly);

    void deleteByIdTrackMonthly(String id);

    void updateTrack(TrackVo trackVo);

    List<TrackMonthly> findAllByTrackId(String id);

    void addTrackMonthly(TrackMonthly trackMonthly);
}
