package net.zlw.cloud.excel.service.impl;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.metadata.Sheet;
import net.zlw.cloud.budgeting.service.BudgetingService;
import net.zlw.cloud.excel.dao.BudgetCoverDao;
import net.zlw.cloud.excel.dao.PartTableQuantitiesDao;
import net.zlw.cloud.excel.dao.SummaryShenjiDao;
import net.zlw.cloud.excel.dao.SummaryUnitsDao;
import net.zlw.cloud.excel.model.*;
import net.zlw.cloud.excel.service.BudgetCoverService;
import net.zlw.cloud.excel.service.SummaryShenjiService;
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
    public void partTableQuantitiesImport() {
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
                    }
                }


            }
            System.out.println(bomTableInfomation.getBomTableList());
            System.out.println(bomTableInfomation);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void LastSummaryCoverImport(String id) {
        try {
            String filePath = "E:\\正量\\新建文件夹\\物料清单表（安徽）.xls";
            //第几张表(最低1) 第几条数据开始(最低0)
            Sheet sheet = new Sheet(2,4);
            FileInputStream fileInputStream = new FileInputStream(new File(filePath));
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            List<Object> read = EasyExcelFactory.read(bufferedInputStream, sheet);
            for (Object o : read) {

            }
        } catch (Exception e) {
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
