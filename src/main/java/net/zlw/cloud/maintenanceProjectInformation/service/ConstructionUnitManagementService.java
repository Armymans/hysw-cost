package net.zlw.cloud.maintenanceProjectInformation.service;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import net.zlw.cloud.maintenanceProjectInformation.mapper.ConstructionUnitManagementMapper;
/**
 * @Author dell
 * @Date 2020/9/27 10:52
 * @Version 1.0
 */
@Service
public class ConstructionUnitManagementService{

    @Resource
    private ConstructionUnitManagementMapper constructionUnitManagementMapper;

}
