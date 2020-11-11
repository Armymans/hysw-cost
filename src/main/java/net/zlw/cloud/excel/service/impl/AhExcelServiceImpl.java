package net.zlw.cloud.excel.service.impl;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.metadata.Sheet;
import net.zlw.cloud.excel.dao.*;
import net.zlw.cloud.excel.model.AnhuiSummarySheet;
import net.zlw.cloud.excel.model.CompetitiveItemValuation;
import net.zlw.cloud.excel.model.SummaryMaterialsSupplied;
import net.zlw.cloud.excel.model.TaxStatement;
import net.zlw.cloud.excel.service.AhExcelService;
import net.zlw.cloud.excelLook.dao.QuantitiesPartialWorksDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Classname AhExcelServiceImpl
 * @Description TODO
 * @Date 2020/11/11 9:36
 * @Created by sjf
 */

@Service
@Transactional
public class AhExcelServiceImpl implements AhExcelService {

    @Autowired
    private AnhuiCoverDao anhuiCoverDao;

    @Autowired
    private AnhuiSummarySheetDao anhuiSummarySheetDao;

    @Autowired
    private QuantitiesPartialWorksDao quantitiesPartialWorksDao;

    @Autowired
    private CompetitiveItemValuationDao competitiveItemValuationDao;

    @Autowired
    private TaxStatementDao taxStatementDao;

    @Autowired
    private SummaryMaterialsSuppliedDao summaryMaterialsSuppliedDao;

    @Override
    public void coverImport(String id) {
        String data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        try {
            String url = "F:\\吴江模板\\安徽表格模板\\预算汇总表-新点（安徽）2.0.xls";
            FileInputStream fileInputStream = new FileInputStream(new File(url));
            Sheet sheet = new Sheet(2,3);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            //读到excel所有数据
            List<Object> read = EasyExcelFactory.read(bufferedInputStream, sheet);

            //读到数据库
//            AnhuiCover anhuiCover = new AnhuiCover();
            AnhuiSummarySheet anhuiSummarySheet = new AnhuiSummarySheet();
            anhuiSummarySheet.setId(UUID.randomUUID().toString().replace("-",""));
            anhuiSummarySheet.setItemName(((List<String>) read.get(0)).get(1));
            anhuiSummarySheet.setPriceSmall(((List<String>) read.get(1)).get(3));
            anhuiSummarySheet.setPriceBig(((List<String>) read.get(2)).get(3));
            anhuiSummarySheet.setConsultingUnit(((List<String>) read.get(3)).get(2));
            anhuiSummarySheet.setAuditor(((List<String>) read.get(4)).get(2));
            anhuiSummarySheet.setPrepareThePeople(((List<String>) read.get(5)).get(2));
            anhuiSummarySheet.setCompileTime(((List<String>) read.get(6)).get(2));
            anhuiSummarySheet.setCreateTime(data);
            anhuiSummarySheet.setBaseProjectId(id);
            anhuiSummarySheet.setStatus("0");
            anhuiSummarySheet.setType("2");
            anhuiSummarySheetDao.insertSelective(anhuiSummarySheet);
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void summarySheetImport(String id) {
        String data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        try {
            String url = "F:\\吴江模板\\安徽表格模板\\预算汇总表-新点（安徽）2.0.xls";
            FileInputStream fileInputStream = new FileInputStream(new File(url));
            Sheet sheet = new Sheet(3, 1);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            List<Object> read = EasyExcelFactory.read(bufferedInputStream, sheet);
            String projectName = "";
            for (int i = 0; i < read.size(); i++) {
                //切割 得到项目名称
                if (i == 0){
                    String s = ((List<String>) read.get(i)).get(0);
                    String[] split = s.split("：");
                    projectName = split[1];
                }

                //判断要读取的位置，进行读取
                if (i > 1){

                    //对不必要读取的数据进行判断跳过
                    if (i == read.size()-2){
                        break;
                    }
                    AnhuiSummarySheet anhuiSummarySheet = new AnhuiSummarySheet();
                    anhuiSummarySheet.setId(UUID.randomUUID().toString().replace("-",""));
                    anhuiSummarySheet.setProjectName(projectName);
                    anhuiSummarySheet.setSerialNumber(((List<String>) read.get(i)).get(0));
                    anhuiSummarySheet.setSummarizing(((List<String>) read.get(i)).get(1));
                    anhuiSummarySheet.setPrice(((List<String>) read.get(i)).get(2));
                    anhuiSummarySheet.setProvisionalEstimate(((List<String>) read.get(i)).get(3));
                    anhuiSummarySheet.setBaseProjectId(id);
                    anhuiSummarySheet.setCreateTime(data);
                    anhuiSummarySheet.setStatus("0");
                    anhuiSummarySheet.setType("2");

                    anhuiSummarySheetDao.insertSelective(anhuiSummarySheet);
                }
            }
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void quantitiesPartialWorksImport(String id) {
        String data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        try {
            String url = "F:\\吴江模板\\安徽表格模板\\预算汇总表-新点（安徽）2.0.xls";
            FileInputStream fileInputStream = new FileInputStream(new File(url));
            Sheet sheet = new Sheet(4,1);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            List<Object> read = EasyExcelFactory.read(bufferedInputStream, sheet);
            String itemName = "";
            for (int i = 0; i < read.size(); i++) {
                //对工程名称进行切割
                if (i == 0){
                    String s = ((List<String>) read.get(i)).get(0);
                    String[] split = s.split("：");
                    itemName = split[1];
                }
                if (i>5){
                    //对不必要读取的数据进行判断跳过
                    if (i == read.size()-1){
                        break;
                    }
                    AnhuiSummarySheet anhuiSummarySheet = new AnhuiSummarySheet();
//                    QuantitiesPartialWorks quantitiesPartialWorks = new QuantitiesPartialWorks();
                    anhuiSummarySheet.setId(UUID.randomUUID().toString().replace("-",""));
                    anhuiSummarySheet.setItemName(itemName);
                    anhuiSummarySheet.setProjectCode(((List<String>) read.get(i)).get(1));
                    anhuiSummarySheet.setProjectName(((List<String>) read.get(i)).get(2));
                    anhuiSummarySheet.setProjectDescription(((List<String>) read.get(i)).get(3));
                    anhuiSummarySheet.setMeasuringUnit(((List<String>) read.get(i)).get(4));
                    anhuiSummarySheet.setQuantities(((List<String>) read.get(i)).get(5));
                    if (((List<String>) read.get(i)).get(6) != null && !"".equals(((List<String>) read.get(i)).get(6))){
                        anhuiSummarySheet.setComprehensiveUnitPrice(new BigDecimal(((List<String>) read.get(i)).get(6)));
                    }
                    if (((List<String>) read.get(i)).get(7) != null && !"".equals(((List<String>) read.get(i)).get(7))){
                        anhuiSummarySheet.setAndPrice(new BigDecimal(((List<String>) read.get(i)).get(7)));
                    }
                    if (((List<String>) read.get(i)).get(8) != null && !"".equals(((List<String>) read.get(i)).get(8))){
                        anhuiSummarySheet.setRateArtificialCost(new BigDecimal(((List<String>) read.get(i)).get(8)));
                    }
                    if (((List<String>) read.get(i)).get(9) != null && !"".equals(((List<String>) read.get(i)).get(9))){
                        anhuiSummarySheet.setFixedMechanicalFee(new BigDecimal(((List<String>) read.get(i)).get(9)));
                    }
                    if (((List<String>) read.get(i)).get(10) != null && !"".equals(((List<String>) read.get(i)).get(10))){
                        anhuiSummarySheet.setTemporaryValuation(new BigDecimal(((List<String>) read.get(i)).get(10)));
                    }

                    anhuiSummarySheet.setBaseProjectId(id);
                    anhuiSummarySheet.setStatus("0");
                    anhuiSummarySheet.setType("2");
                    anhuiSummarySheetDao.insertSelective(anhuiSummarySheet);
                }
            }
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void competitiveItemValuationImport(String id) {
        String data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());


        try {
            String url = "F:\\吴江模板\\安徽表格模板\\预算汇总表-新点（安徽）2.0.xls";
            FileInputStream fileInputStream = new FileInputStream(new File(url));
            Sheet sheet = new Sheet(5, 1);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            List<Object> read = EasyExcelFactory.read(bufferedInputStream, sheet);
            String itemName = "";
            for (int i = 0; i < read.size(); i++) {
                if (i == 0){
                    String s = ((List<String>) read.get(i)).get(0);
                    String[] split = s.split("：");
                    itemName = split[1];
                }
                if (i == read.size()-2){
                    break;
                }

                System.out.println(((List<String>) read.get(i)));
                System.out.println(((List<String>) read.get(i)));
                System.out.println(((List<String>) read.get(i)));
                System.out.println(((List<String>) read.get(i)));
                System.out.println(((List<String>) read.get(i)));
                System.out.println(((List<String>) read.get(i)));
                if (i > 1){
                    CompetitiveItemValuation competitiveItemValuation = new CompetitiveItemValuation();
                    competitiveItemValuation.setId(UUID.randomUUID().toString().replace("-",""));
                    competitiveItemValuation.setItemName(itemName);
                    competitiveItemValuation.setProjectCode(((List<String>) read.get(i)).get(1));
                    competitiveItemValuation.setProjectName(((List<String>) read.get(i)).get(3));
                    if (((List<String>) read.get(i)).get(5) !=null && !"".equals(((List<String>) read.get(i)).get(5))){
                        competitiveItemValuation.setComputationalBase(new BigDecimal(((List<String>) read.get(i)).get(5)));
                    }
                    if (((List<String>) read.get(i)).get(6) !=null && !"".equals(((List<String>) read.get(i)).get(6))){
                        competitiveItemValuation.setRate(new BigDecimal(((List<String>) read.get(i)).get(6)));
                    }
                    if (((List<String>) read.get(i)).get(8) !=null && !"".equals(((List<String>) read.get(i)).get(8))){
                        competitiveItemValuation.setAmount(new BigDecimal(((List<String>) read.get(i)).get(5)));
                    }
                    competitiveItemValuation.setForeignKey(id);
                    competitiveItemValuationDao.insertSelective(competitiveItemValuation);
                }
            }
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void taxStatementImport(String id) {
        String data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        try {
            String url = "F:\\吴江模板\\安徽表格模板\\预算汇总表-新点（安徽）2.0.xls";
            FileInputStream fileInputStream = new FileInputStream(new File(url));
            Sheet sheet = new Sheet(6, 1);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            List<Object> read = EasyExcelFactory.read(bufferedInputStream, sheet);
            String itemName = "";
            for (int i = 0; i < read.size()-2; i++) {
                if (i==0){
                    String s = ((List<String>) read.get(i)).get(0);
                    String[] split = s.split("：");
                    itemName = split[1];
                }
                if (i>1){
                    TaxStatement taxStatement = new TaxStatement();
                    taxStatement.setId(UUID.randomUUID().toString().replace("-",""));
                    taxStatement.setProjectName(((List<String>) read.get(i)).get(1));
                    taxStatement.setItemName(itemName);
                    taxStatement.setCalculationBasis(((List<String>) read.get(i)).get(3));
                    taxStatement.setComputationalBase(((List<String>) read.get(i)).get(5));
                    if (((List<String>) read.get(i)).get(7) != null && !"".equals(((List<String>) read.get(i)).get(7))){
                        taxStatement.setRate(new BigDecimal(((List<String>) read.get(i)).get(7)));
                    }
                    if (((List<String>) read.get(i)).get(8) != null && !"".equals(((List<String>) read.get(i)).get(8))){
                        taxStatement.setAmount(new BigDecimal(((List<String>) read.get(i)).get(8)));
                    }
                    taxStatement.setForeignKey(id);
                    taxStatementDao.insertSelective(taxStatement);
                }
            }
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void summaryMaterialsSuppliedAImport(String id) {
        try {
            String url = "F:\\吴江模板\\安徽表格模板\\预算汇总表-新点（安徽）2.0.xls";
            FileInputStream fileInputStream = new FileInputStream(new File(url));
            Sheet sheet = new Sheet(7, 1);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            List<Object> read = EasyExcelFactory.read(bufferedInputStream, sheet);
            String itemName = "";
            for (int i = 0; i < read.size(); i++) {
                if (i==0){
                    String s = ((List<String>) read.get(i)).get(0);
                    String[] split = s.split("：");
                    itemName = split[1];
                }
                if (i > 2){
                    SummaryMaterialsSupplied summaryMaterialsSupplied = new SummaryMaterialsSupplied();
                    summaryMaterialsSupplied.setId(UUID.randomUUID().toString().replace("-",""));
                    summaryMaterialsSupplied.setItemName(itemName);
                    summaryMaterialsSupplied.setNameMaterial(((List<String>) read.get(i)).get(1));
                    summaryMaterialsSupplied.setSpecialRequirements(((List<String>) read.get(i)).get(2));
                    summaryMaterialsSupplied.setUnit(((List<String>) read.get(i)).get(3));
                    summaryMaterialsSupplied.setNumberOf(((List<String>) read.get(i)).get(4));
                    if (((List<String>) read.get(i)).get(5) != null && !"".equals(((List<String>) read.get(i)).get(5))){
                        summaryMaterialsSupplied.setPrice(new BigDecimal(((List<String>) read.get(i)).get(5)));
                    }
                    if (((List<String>) read.get(i)).get(6) != null && !"".equals(((List<String>) read.get(i)).get(6))){
                        summaryMaterialsSupplied.setAndPrice(new BigDecimal(((List<String>) read.get(i)).get(6)));
                    }
                    summaryMaterialsSupplied.setAB("1");
                    summaryMaterialsSupplied.setForeignKey(id);
                    summaryMaterialsSuppliedDao.insertSelective(summaryMaterialsSupplied);
                }
            }
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void summaryMaterialsSuppliedBImport(String id) {
        try {
            String url = "F:\\吴江模板\\安徽表格模板\\预算汇总表-新点（安徽）2.0.xls";
            FileInputStream fileInputStream = new FileInputStream(new File(url));
            Sheet sheet = new Sheet(7, 1);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            List<Object> read = EasyExcelFactory.read(bufferedInputStream, sheet);
            String itemName = "";
            for (int i = 0; i < read.size(); i++) {
                if (i==0){
                    String s = ((List<String>) read.get(i)).get(0);
                    String[] split = s.split("：");
                    itemName = split[1];
                }
                if (i > 2){
                    SummaryMaterialsSupplied summaryMaterialsSupplied = new SummaryMaterialsSupplied();
                    summaryMaterialsSupplied.setId(UUID.randomUUID().toString().replace("-",""));
                    summaryMaterialsSupplied.setItemName(itemName);
                    summaryMaterialsSupplied.setNameMaterial(((List<String>) read.get(i)).get(1));
                    summaryMaterialsSupplied.setSpecialRequirements(((List<String>) read.get(i)).get(2));
                    summaryMaterialsSupplied.setUnit(((List<String>) read.get(i)).get(3));
                    summaryMaterialsSupplied.setNumberOf(((List<String>) read.get(i)).get(4));
                    if (((List<String>) read.get(i)).get(5) != null && !"".equals(((List<String>) read.get(i)).get(5))){
                        summaryMaterialsSupplied.setPrice(new BigDecimal(((List<String>) read.get(i)).get(5)));
                    }
                    if (((List<String>) read.get(i)).get(6) != null && !"".equals(((List<String>) read.get(i)).get(6))){
                        summaryMaterialsSupplied.setAndPrice(new BigDecimal(((List<String>) read.get(i)).get(6)));
                    }
                    summaryMaterialsSupplied.setAB("2");
                    summaryMaterialsSupplied.setForeignKey(id);
                    summaryMaterialsSuppliedDao.insertSelective(summaryMaterialsSupplied);
                }
            }
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
