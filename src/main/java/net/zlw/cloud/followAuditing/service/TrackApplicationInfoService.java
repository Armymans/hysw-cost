package net.zlw.cloud.followAuditing.service;


import com.github.pagehelper.PageInfo;
import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;
import net.zlw.cloud.followAuditing.model.TrackMonthly;
import net.zlw.cloud.followAuditing.model.vo.PageVo;
import net.zlw.cloud.followAuditing.model.vo.ReturnTrackVo;
import net.zlw.cloud.followAuditing.model.vo.TrackVo;

import java.util.List;

public interface TrackApplicationInfoService {
    PageInfo<ReturnTrackVo> selectTrackList(PageVo pageVo);


    void deleteById(String id);

    void batchReview(BatchReviewVo id);

    void addTrack(TrackVo trackVo);

    TrackVo selectTrackById(String id);

    void updateMonthly(TrackMonthly trackMonthly);

    void updateTrack(TrackVo trackVo);
}
