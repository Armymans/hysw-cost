package net.zlw.cloud.excel.service.impl;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.metadata.Sheet;
import net.zlw.cloud.excel.dao.*;
import net.zlw.cloud.excel.model.*;
import net.zlw.cloud.excel.service.BudgetCoverService;
import net.zlw.cloud.excel.util.RMBdeal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@Transactional
public class BudgetCoverServiceimpl implements BudgetCoverService {
    @Resource
    private BudgetCoverDao budgetCoverDao;
    @Resource
    private SummaryShenjiDao summaryShenjiDao;
    @Resource
    private SummaryUnitsDao summaryUnitsDao;
    @Resource
    private PartTableQuantitiesDao partTableQuantitiesDao;
    @Resource
    private BomTable1Dao bomTable1Dao;
    @Resource
    private BomTableInfomationDao bomTableInfomationDao;
    @Resource
    private LastSummaryCoverDao lastSummaryCoverDao;
    @Resource
    private UnitProjectSummaryDao unitProjectSummaryDao;
    @Resource
    private SummaryTableDao summaryTableDao;
    @Resource
    private VerificationSheetDao verificationSheetDao;
    @Resource
    private VerificationSheetProjectDao verificationSheetProjectDao;


    private String[] hanArr = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
    private String[] unitArr = { "分","角","拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟" };

    private String toHanStr(String numStr) {

        String result = "";
        int numLen = numStr.length();

        for (int i = 0; i < numLen; i++) {

            int num = numStr.charAt(i) - 48;

            if (i != numLen - 1 && num != 0) {
                result += hanArr[num] + unitArr[numLen - 2 - i];
            } else {
                result += hanArr[num];
            }
        }
        return result;
    }



    @Override
    public void coverImport() {
        String url = "E:\\正量\\新建文件夹\\预算汇总表-神机（安徽）.xlsx";
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(url));
            Sheet sheet = new Sheet(2,4);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            List<Object> read = EasyExcelFactory.read(bufferedInputStream, sheet);
            SummaryShenji summaryShenji = new SummaryShenji();
            String s = extractingText((List<String>) read.get(8));
            String s2 = extractingText((List<String>) read.get(9));
            String s1 = extractingText((List<String>) read.get(11));

//            ((List<String>) read.get(0)).get(0);
            summaryShenji.setId(UUID.randomUUID().toString().replace("-",""));
            summaryShenji.setConstructionUnit(((List<String>) read.get(0)).get(3));
            summaryShenji.setProjectName(((List<String>) read.get(1)).get(3));
            summaryShenji.setProjectSite(((List<String>) read.get(2)).get(3));
            String s3 = ((List<String>) read.get(5)).get(4);
            String substring = s3.substring(0, s3.length() - 1);
            summaryShenji.setAmountCostLowercase(new BigDecimal(substring));
            summaryShenji.setAmountCostCapital(((List<String>) read.get(6)).get(4));
            summaryShenji.setUnit(((List<String>) read.get(7)).get(3));
            summaryShenji.setAuditor(s);
            summaryShenji.setPreparePeople(s2);
            summaryShenji.setCompileTime(s1);
            summaryShenji.setDelFlag("0");
            System.out.println(summaryShenji);
            summaryShenjiDao.insertSelective(summaryShenji);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void summaryUnitsImport() {
        String url = "E:\\正量\\新建文件夹\\预算汇总表-神机（安徽）.xlsx";
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(url));
            Sheet sheet = new Sheet(3,4);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            List<Object> read = EasyExcelFactory.read(bufferedInputStream, sheet);
            String projectName = "";
            for (int i = 0; i < read.size(); i++) {
                if (i==0){
                    String s = ((List<String>) read.get(i)).get(0);
                    String[] split = s.split("：");
                    projectName = split[1];
                }
                if (i >= 2){
                    SummaryUnits summaryUnits = new SummaryUnits();
                    summaryUnits.setId(UUID.randomUUID().toString().replace("-",""));
                    summaryUnits.setSerialNumber(((List<String>) read.get(i)).get(0));
                    summaryUnits.setProjectName(projectName);
                    summaryUnits.setAggregateContent(((List<String>) read.get(i)).get(1));
                    summaryUnits.setDelFlag("0");
                    if (((List<String>) read.get(i)).get(2)!=null){
                        summaryUnits.setAmount(new BigDecimal(((List<String>) read.get(i)).get(2)));
                    }
                   summaryUnitsDao.insertSelective(summaryUnits);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void partTableQuantitiesImport(String id) {
        String url = "E:\\正量\\新建文件夹\\预算汇总表-神机（安徽）.xlsx";
        String projectName = "";
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(url));
            Sheet sheet = new Sheet(4,4);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            List<Object> read = EasyExcelFactory.read(bufferedInputStream, sheet);
            for (int i = 0; i < read.size(); i++) {

                if (i==0){
                    String s = ((List<String>) read.get(i)).get(0);
                    String[] split = s.split("：");
                    projectName = split[1];
                }

                if (i>=4){
                    String f = "^[+-]?\\d+(\\d+)?$";
                    Pattern compile = Pattern.compile(f);
                    if (((List<String>)read.get(i)).get(0)!=null && compile.matcher(((List<String>)read.get(i)).get(0)).matches()){
                        System.out.println(((List<String>)read.get(i)).get(0));
                        PartTableQuantities partTableQuantities = new PartTableQuantities();
                        partTableQuantities.setId(UUID.randomUUID().toString().replace("-",""));

                        partTableQuantities.setSerialNumber(((List<String>)read.get(i)).get(0));
                        partTableQuantities.setProjectCode(((List<String>)read.get(i)).get(1));
                        partTableQuantities.setProjectName(((List<String>)read.get(i)).get(2));
                        partTableQuantities.setMeasurement(((List<String>)read.get(i)).get(3));
                        partTableQuantities.setEngineering(((List<String>)read.get(i)).get(4));
                        if (((List<String>) read.get(i)).get(5)!=null){
                            BigDecimal bigDecimal = new BigDecimal(((List<String>) read.get(i)).get(5));
                            partTableQuantities.setComprehensivePrice(bigDecimal);
                        }
                        if (((List<String>) read.get(i)).get(6)!=null){
                            BigDecimal bigDecimal1 = new BigDecimal(((List<String>) read.get(i)).get(6));
                            partTableQuantities.setCombinedPrice(bigDecimal1);
                        }
                        if (((List<String>) read.get(i)).get(7)!=null){
                            BigDecimal bigDecimal2 = new BigDecimal(((List<String>) read.get(i)).get(7));
                            partTableQuantities.setArtificialCost(bigDecimal2);
                        }
                        partTableQuantities.setForeignKey(id);
                        partTableQuantities.setDelFlag("0");
                        partTableQuantitiesDao.insertSelective(partTableQuantities);

                        System.out.println(partTableQuantities);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void bomTableImport(String id) {
        try {
            String filePath = "E:\\正量\\新建文件夹\\物料清单表（安徽）.xls";
            //第几张表(最低1) 第几条数据开始(最低0)
            Sheet sheet = new Sheet(1,1);
            FileInputStream fileInputStream = new FileInputStream(new File(filePath));
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            List<Object> read = EasyExcelFactory.read(bufferedInputStream, sheet);
            BomTableInfomation bomTableInfomation = new BomTableInfomation();
            bomTableInfomation.setId(UUID.randomUUID().toString().replace("-",""));
            bomTableInfomation.setBusinessProcess(((List<String>) read.get(0)).get(2));
            bomTableInfomation.setDateOf(((List<String>) read.get(1)).get(2));
            bomTableInfomation.setSalesOrganization(((List<String>) read.get(2)).get(2));
            bomTableInfomation.setInventoryOrganization(((List<String>) read.get(3)).get(2));
            bomTableInfomation.setCeaNum(((List<String>) read.get(4)).get(2));
            bomTableInfomation.setAcquisitionTypes(((List<String>) read.get(5)).get(2));
            bomTableInfomation.setContractor(((List<String>) read.get(6)).get(2));
            bomTableInfomation.setProjectCategoriesCoding(((List<String>) read.get(7)).get(2));
            bomTableInfomation.setProjectTypes(((List<String>) read.get(8)).get(2));
            bomTableInfomation.setItemCoding(((List<String>) read.get(9)).get(2));
            bomTableInfomation.setProjectName(((List<String>) read.get(10)).get(2));
            bomTableInfomation.setAcquisitionDepartment(((List<String>) read.get(11)).get(2));
            bomTableInfomation.setRemark(((List<String>) read.get(12)).get(2));
            bomTableInfomation.setBudgetId(id);
            System.out.println(bomTableInfomation);
            bomTableInfomation.setDelFlag("0");
            bomTableInfomation.setBomTableList(new ArrayList<BomTable>());
            for (int i = 0; i < read.size(); i++) {
                List<String> o1 = (List<String>) read.get(i);
                String f = "^[+-]?\\d+(\\d+)?$";
                Pattern compile = Pattern.compile(f);
                if (i>=14){
                    if (compile.matcher(o1.get(0)).matches()){
                        List<BomTable> bomTableList = bomTableInfomation.getBomTableList();
                        BomTable bomTable = new BomTable(UUID.randomUUID().toString().replace("-", ""), o1.get(1), o1.get(2), o1.get(3), o1.get(4), o1.get(5), o1.get(6), bomTableInfomation.getId());
                        bomTable.setDelFlag("0");
                        bomTable.setMaterialCode(o1.get(0));
                        bomTableList.add(bomTable);
                        bomTable1Dao.insertSelective(bomTable);
                    }
                }


            }
            bomTableInfomationDao.insertSelective(bomTableInfomation);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void LastSummaryCoverImport(String id) {
        try {
            String filePath = "E:\\正量\\新建文件夹\\上家结算汇总表（安徽）.xlsx";
            //第几张表(最低1) 第几条数据开始(最低0)
            Sheet sheet = new Sheet(2,4);
            FileInputStream fileInputStream = new FileInputStream(new File(filePath));
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            List<Object> read = EasyExcelFactory.read(bufferedInputStream, sheet);
            LastSummaryCover lastSummaryCover = new LastSummaryCover();
            lastSummaryCover.setId(UUID.randomUUID().toString().replace("-",""));
            String s = extractingText((List<String>) read.get(0));
            lastSummaryCover.setConstructionUnit(s);
            String s1 = extractingText((List<String>) read.get(1));
            lastSummaryCover.setNameProject(s1);
            String s5 = ((List<String>) read.get(4)).get(4);
            String substring = s5.substring(0, s5.length() - 1);
            BigDecimal bigDecimal = new BigDecimal(substring);
            lastSummaryCover.setAmountCostLowercase(bigDecimal);
            lastSummaryCover.setAmountCostCapital(((List<String>)read.get(5)).get(4));
            String s6 = extractingText((List<String>) read.get(6));
            lastSummaryCover.setUnit(s6);
            String s2 = extractingText((List<String>) read.get(10));
            lastSummaryCover.setPreparePeople(s2);
            String s3 = extractingText((List<String>) read.get(8));
            lastSummaryCover.setReviewer(s3);
            String s4 = extractingText((List<String>) read.get(13));
            lastSummaryCover.setCompileTime(s4);
            lastSummaryCover.setLastSettlementId(id);
            lastSummaryCover.setDelFlag("0");
            System.out.println(lastSummaryCover);
            lastSummaryCoverDao.insertSelective(lastSummaryCover);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void UnitProjectSummaryImport(String id) {
        try {
            String filePath = "E:\\正量\\新建文件夹\\上家结算汇总表（安徽）.xlsx";
            //第几张表(最低1) 第几条数据开始(最低0)
            Sheet sheet = new Sheet(3, 4);
            FileInputStream fileInputStream = new FileInputStream(new File(filePath));
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            List<Object> read = EasyExcelFactory.read(bufferedInputStream, sheet);
            String projectName = "";
            for (int i = 0; i < read.size(); i++) {

                if(i==0){
                    projectName = ((List<String>)read.get(i)).get(0).split("：")[1];
                }
                if (i >= 2){
                    UnitProjectSummary unitProjectSummary = new UnitProjectSummary();
                    unitProjectSummary.setId(UUID.randomUUID().toString().replace("-",""));
                    unitProjectSummary.setSerialNumber(((List<String>)read.get(i)).get(0));
                    unitProjectSummary.setEngineeringName(projectName);
                    unitProjectSummary.setProjectName(((List<String>)read.get(i)).get(1));
                    if (((List<String>) read.get(i)).get(2)!=null){
                        BigDecimal bigDecimal = new BigDecimal(((List<String>) read.get(i)).get(2));
                        unitProjectSummary.setAmount(bigDecimal);
                    }
                    unitProjectSummary.setLastSettlementId(id);
                    unitProjectSummary.setDelFlag("0");
                    unitProjectSummary.setLastSettlementId(id);
                    unitProjectSummaryDao.insertSelective(unitProjectSummary);
                    System.out.println(unitProjectSummary);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void summaryTableImport(String id) {
        try {
            String filePath = "E:\\正量\\新建文件夹\\下家结算审核汇总表（安徽）.xls";
            //第几张表(最低1) 第几条数据开始(最低0)
            Sheet sheet = new Sheet(1, 0);
            FileInputStream fileInputStream = new FileInputStream(new File(filePath));
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            List<Object> read = EasyExcelFactory.read(bufferedInputStream, sheet);
            String projectName = "";


            //调用核定单的工程名称
            String filePath1 = "E:\\正量\\新建文件夹\\下家结算审核汇总表（安徽）.xls";
            //第几张表(最低1) 第几条数据开始(最低0)
            Sheet sheet1 = new Sheet(2, 3);
            FileInputStream fileInputStream1 = new FileInputStream(new File(filePath1));
            BufferedInputStream bufferedInputStream1 = new BufferedInputStream(fileInputStream1);
            List<Object> read1 = EasyExcelFactory.read(bufferedInputStream1, sheet1);
            projectName = ((List<String>)read1.get(2)).get(2);
            BigDecimal bigDecimal2 = new BigDecimal(0.00);
            for (int i = 0; i < read.size(); i++) {

                if (i >= 3){
                    SummaryTable summaryTable = new SummaryTable();
                    summaryTable.setId(UUID.randomUUID().toString().replace("-",""));
                    summaryTable.setSerialNumber(((List<String>)read.get(i)).get(0));
                    summaryTable.setEngineeringName(projectName);
                    summaryTable.setProjectName(((List<String>)read.get(i)).get(1));
                    if (((List<String>) read.get(i)).get(2)!=null){
                        BigDecimal bigDecimal = new BigDecimal(((List<String>) read.get(i)).get(2));
                        summaryTable.setReviewAmount(bigDecimal);
                    }
                    if (((List<String>) read.get(i)).get(3)!=null){
                        BigDecimal bigDecimal1 = new BigDecimal(((List<String>) read.get(i)).get(3));
                        summaryTable.setAuthorizedAmount(bigDecimal1);
                    }
                    if (i == 3){
                        if (((List<String>) read.get(i)).get(4)!=null){
                            bigDecimal2 = new BigDecimal(((List<String>) read.get(i)).get(4));
                            summaryTable.setNuclearIncreasingOrDecreasing(bigDecimal2);
                            summaryTable.setRemark(((List<String>) read.get(i)).get(5));
                            summaryTable.setSettlementId(id);
                            summaryTableDao.insertSelective(summaryTable);

                        }
                    }
                    if (i == 4){
                        summaryTable.setNuclearIncreasingOrDecreasing(bigDecimal2);
                        summaryTable.setRemark(((List<String>) read.get(i)).get(5));
                        summaryTable.setSettlementId(id);
                        summaryTableDao.insertSelective(summaryTable);

                    }
                    if (i > 4){
                        BigDecimal bigDecimal = new BigDecimal(((List<String>) read.get(i)).get(4));
                        summaryTable.setNuclearIncreasingOrDecreasing(bigDecimal);
                        summaryTable.setRemark(((List<String>) read.get(i)).get(5));
                        summaryTable.setSettlementId(id);
                        summaryTableDao.insertSelective(summaryTable);
                    }

                }


            }




        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void verificationSheetImport(String id) {
        RMBdeal rmBdeal = new RMBdeal();
        try {
            String filePath = "E:\\正量\\新建文件夹\\下家结算审核汇总表（安徽）.xls";
            //第几张表(最低1) 第几条数据开始(最低0)
            Sheet sheet = new Sheet(2, 1);
            FileInputStream fileInputStream = new FileInputStream(new File(filePath));
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            List<Object> read = EasyExcelFactory.read(bufferedInputStream, sheet);
            VerificationSheet verificationSheet = new VerificationSheet();
            verificationSheet.setId(UUID.randomUUID().toString().replace("-",""));
            verificationSheet.setConstructionUnit(((List<String>)read.get(1)).get(2));
            verificationSheet.setType(((List<String>)read.get(1)).get(7));
            verificationSheet.setConstructionOrganization(((List<String>)read.get(2)).get(2));
            verificationSheet.setProfessional(((List<String>)read.get(2)).get(7));
            verificationSheet.setProjectName(((List<String>)read.get(3)).get(2));
            verificationSheet.setCeaNum(((List<String>)read.get(3)).get(7));
            int i = 0;
            for (int i1 = 0; i1 < read.size(); i1++) {
                boolean f = ((List<String>) read.get(i1)).get(0).contains("合");
                System.out.println(((List<String>) read.get(i1)).get(0));
                if (f){
                    i = i1;
                    break;
                }


                if (i1 >= 5){
                    VerificationSheetProject verificationSheetProject = new VerificationSheetProject();
                    verificationSheetProject.setId(UUID.randomUUID().toString().replace("-",""));
                    verificationSheetProject.setSerialNumber(((List<String>) read.get(i1)).get(0));
                    verificationSheetProject.setProjectUnit(((List<String>) read.get(i1)).get(1));
                    if (((List<String>) read.get(i1)).get(3)!=null && !((List<String>) read.get(i1)).get(3).equals("")){
                        BigDecimal bigDecimal = new BigDecimal(((List<String>) read.get(i1)).get(3));
                        verificationSheetProject.setReviewNumber(bigDecimal);
                    }
                    if (((List<String>) read.get(i1)).get(4)!=null && ! "" .equals(((List<String>) read.get(i1)).get(4))){
                        BigDecimal bigDecimal1 = new BigDecimal(((List<String>) read.get(i1)).get(4));
                        verificationSheetProject.setAuthorizedNumber(bigDecimal1);
                    }
                    if (((List<String>) read.get(i1)).get(5)!=null  && ! "" .equals(((List<String>) read.get(i1)).get(5))){
                        BigDecimal bigDecimal2 = new BigDecimal(((List<String>) read.get(i1)).get(5));
                        verificationSheetProject.setSubtractNumber(bigDecimal2);
                    }
                    if (((List<String>) read.get(i1)).get(6)!=null  && ! "" .equals(((List<String>) read.get(i1)).get(6))){
                        BigDecimal bigDecimal3 = new BigDecimal(((List<String>) read.get(i1)).get(6));
                        verificationSheetProject.setNuclearNumber(bigDecimal3);
                    }
                    if (((List<String>) read.get(i1)).get(7)!=null  && ! "" .equals(((List<String>) read.get(i1)).get(7))){
                        BigDecimal bigDecimal4 = new BigDecimal(((List<String>) read.get(i1)).get(7));
                        verificationSheetProject.setIncreaseReduction(bigDecimal4);
                    }
                    verificationSheetProject.setVerificationSheetId(verificationSheet.getId());
                    verificationSheetProject.setDelFlag("0");
                    verificationSheetProjectDao.insertSelective(verificationSheetProject);
                }
            }
            System.err.println(i);
            String total = (((List<String>)read.get(i)).get(3))+"#"+(((List<String>)read.get(i)).get(4))+"#"+(((List<String>)read.get(i)).get(5))+"#"+(((List<String>)read.get(i)).get(6))+"#"+(((List<String>)read.get(i)).get(7));
            verificationSheet.setTotal(total);
            String s1 = rmBdeal.deal4RMB(((List<String>) read.get(i)).get(4));
            verificationSheet.setAuthorizedAmount(s1);
            BigDecimal bigDecimal = new BigDecimal(((List<String>) read.get(i + 2)).get(2));
            verificationSheet.setSubtractForehead(bigDecimal);
            String s2 = ((List<String>) read.get(i + 2)).get(4);
            String substring = s2.substring(0, s2.length() - 1);
            BigDecimal bigDecimal2 = new BigDecimal(substring);
            verificationSheet.setSubtractRate(bigDecimal2);
            BigDecimal bigDecimal3 = new BigDecimal(((List<String>) read.get(i + 2)).get(7));
            verificationSheet.setExcessVerificationReduction(bigDecimal3);
            BigDecimal bigDecimal1 = new BigDecimal(((List<String>) read.get(i + 3)).get(3));
            verificationSheet.setActualSettlementProject(bigDecimal1);
            verificationSheet.setUpperCase(rmBdeal.deal4RMB(((List<String>) read.get(i + 3)).get(3)));
            verificationSheet.setAuditor(((List<String>) read.get(i + 4)).get(4));
            verificationSheet.setConstructionOrganizationChapter(((List<String>) read.get(i + 4)).get(6));
            verificationSheet.setReviewer(((List<String>) read.get(i + 7)).get(4));
            verificationSheet.setOperator(((List<String>) read.get(i + 7)).get(6));
            verificationSheet.setModdate(((List<String>) read.get(i + 8)).get(3).split("：")[1]);
            verificationSheet.setDatePossession(((List<String>) read.get(i + 8)).get(6).split("：")[1]);
            verificationSheet.setDevelopmentOrganizationChapter(((List<String>) read.get(i + 9)).get(4));
            verificationSheet.setProjectLeader(((List<String>) read.get(i + 10)).get(4));
            verificationSheet.setSettlementId(id);
            verificationSheet.setDelFlag("0");
            verificationSheetDao.insertSelective(verificationSheet);
            System.out.println(verificationSheet);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    //提取文字
    public String extractingText(List<String> list){
        for (String textContent : list) {
            if (textContent!=null){
                textContent = textContent.trim();
                while (textContent.startsWith("　")) {//这里判断是不是全角空格
                    textContent = textContent.substring(1, textContent.length()).trim();
                }
                while (textContent.endsWith("　")) {
                    textContent = textContent.substring(0, textContent.length() - 1).trim();
                }
                String s = "[\u4e00-\u9fa5]+";
                String pattern = "\\d{4}(\\.|\\/|.)\\d{1,2}\\1\\d{1,2}";
                Pattern compile = Pattern.compile(s);
                Pattern compile1 = Pattern.compile(pattern);
                if (compile.matcher(textContent).matches()){
                        return textContent;
                }
                if (compile1.matcher(textContent).matches()){
                    return textContent;
                }

            }

        }
        return null;
    }
}
