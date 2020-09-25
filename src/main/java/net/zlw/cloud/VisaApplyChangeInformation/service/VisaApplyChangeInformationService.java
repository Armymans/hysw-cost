package net.zlw.cloud.VisaApplyChangeInformation.service;

import net.zlw.cloud.VisaApplyChangeInformation.mapper.VisaApplyChangeInformationMapper;
import net.zlw.cloud.VisaApplyChangeInformation.model.VisaApplyChangeInformation;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by xulei on 2020/9/22.
 */
@Service
public class VisaApplyChangeInformationService {

    @Resource
    private VisaApplyChangeInformationMapper visaApplyChangeInformationMapper;


    public List<VisaApplyChangeInformation> findVaciList() {
        return visaApplyChangeInformationMapper.selectAll();
    }
}
