package net.zlw.cloud.followAuditing.service;


import com.github.pagehelper.PageInfo;
import net.tec.cloud.common.bean.UserInfo;
import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;
import net.zlw.cloud.followAuditing.model.TrackMonthly;
import net.zlw.cloud.followAuditing.model.vo.AuditInfoVo;
import net.zlw.cloud.followAuditing.model.vo.PageVo;
import net.zlw.cloud.followAuditing.model.vo.ReturnTrackVo;
import net.zlw.cloud.followAuditing.model.vo.TrackVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface TrackApplicationInfoService {
    PageInfo<ReturnTrackVo> selectTrackList(PageVo pageVo);

    void deleteById(String id,UserInfo loginUser,HttpServletRequest request);

    void batchReview(BatchReviewVo id,UserInfo userInfo,HttpServletRequest request);

    void addTrack(TrackVo trackVo, UserInfo userInfo, String baseId, HttpServletRequest request) throws Exception;

    TrackVo selectTrackById(String id,UserInfo userInfo);
    TrackVo editTrackById(String id,UserInfo userInfo);

    List<AuditInfoVo> findAllAuditInfosByTrackId(String id);

    void updateMonthly(TrackMonthly trackMonthly);

    void deleteByIdTrackMonthly(String id);

    void updateTrack(TrackVo trackVo,UserInfo userInfo,HttpServletRequest request) throws Exception;

    List<TrackMonthly> findAllByTrackId(String id);

    void addTrackMonthly(TrackMonthly trackMonthly);

    List<TrackMonthly> findAllByTrackId3(String id,UserInfo userInfo);

    void deleteMonthly(String id);

    void addAttribution(String baseId, String district, String designCategory, String prePeople);
}
