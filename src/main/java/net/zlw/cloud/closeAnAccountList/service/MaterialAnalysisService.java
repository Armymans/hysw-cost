package net.zlw.cloud.closeAnAccountList.service;

import net.zlw.cloud.excel.dao.MaterialAnalysisDao;
import net.zlw.cloud.excel.model.MaterialAnalysis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @Classname MaterialAnalysisService
 * @Description TODO
 * @Date 2020/11/3 15:27
 * @Created by sjf
 */

@Service
@Transactional
public class MaterialAnalysisService {

    @Autowired
    private MaterialAnalysisDao materialAnalysisDao;


    public List<MaterialAnalysis> findMaterialAnalysisList(String id){
        return materialAnalysisDao.selectMaterialAnalysisById(id);
    }
}
