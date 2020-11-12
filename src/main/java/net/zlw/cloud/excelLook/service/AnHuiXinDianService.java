package net.zlw.cloud.excelLook.service;

import net.zlw.cloud.excel.dao.*;
import net.zlw.cloud.excel.model.*;
import net.zlw.cloud.excelLook.dao.QuantitiesPartialWorksDao;
import net.zlw.cloud.excelLook.domain.QuantitiesPartialWorks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Classname AnHuiXinDianService
 * @Description TODO
 * @Date 2020/11/12 9:17
 * @Created by sjf
 */

@Service
@Transactional
public class AnHuiXinDianService {

    @Autowired
    private AnhuiCoverDao anhuiCoverDao;

    @Autowired
    private QuantitiesPartialWorksDao quantitiesPartialWorksDao;

    @Autowired
    private AnhuiSummarySheetDao anhuiSummarySheetDao;

    @Autowired
    private CompetitiveItemValuationDao competitiveItemValuationDao;

    @Autowired
    private TaxStatementDao taxStatementDao;

    @Autowired
    private SummaryMaterialsSuppliedDao summaryMaterialsSuppliedDao;

    public AnhuiCover findOne(String id){
       return anhuiCoverDao.findOne(id);
    }

    public List<AnhuiSummarySheet> findList(String id){
        return anhuiSummarySheetDao.findList(id);
    }


    public List<QuantitiesPartialWorks> findWorksList(String id) {
         return quantitiesPartialWorksDao.findWorksLists(id);
    }

    public List<CompetitiveItemValuation> competitiveItemValuationList(String id) {
     return competitiveItemValuationDao.findList(id);
    }

    public List<TaxStatement> taxStatementList(String id) {
        return taxStatementDao.findTaxStatementList(id);
    }

    public List<SummaryMaterialsSupplied> MaterialsSuppliedAList(String id) {
        return summaryMaterialsSuppliedDao.findAList(id);
    }

    public List<SummaryMaterialsSupplied> MaterialsSuppliedBList(String id) {
        return summaryMaterialsSuppliedDao.findBList(id);
    }


}
