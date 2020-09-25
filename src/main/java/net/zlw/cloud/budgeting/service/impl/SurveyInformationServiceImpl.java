package net.zlw.cloud.budgeting.service.impl;

import net.zlw.cloud.budgeting.mapper.SurveyInformationDao;
import net.zlw.cloud.budgeting.service.SurveyInformationSerivce;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class SurveyInformationServiceImpl implements SurveyInformationSerivce {
    @Resource
    private SurveyInformationDao surveyInformationDao;

}
