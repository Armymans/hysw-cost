package net.zlw.cloud.excel.service.impl;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.metadata.Sheet;
import net.zlw.cloud.excel.dao.LastSummaryCoverDao;
import net.zlw.cloud.excel.dao.PartTableQuantitiesDao;
import net.zlw.cloud.excel.dao.SummaryUnitsDao;
import net.zlw.cloud.excel.model.LastSummaryCover;
import net.zlw.cloud.excel.model.PartTableQuantities;
import net.zlw.cloud.excel.model.SummaryUnits;
import net.zlw.cloud.excel.service.WjExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * @Classname WjExcelServiceImpl
 * @Description TODO
 * @Date 2020/11/10 10:00
 * @Created by sjf
 */
@Service
@Transactional
public class WjExcelServiceImpl implements WjExcelService {

    @Autowired
    private LastSummaryCoverDao lastSummaryCoverDao;

    @Autowired
    private SummaryUnitsDao summaryUnitsDao;

    @Autowired
    private PartTableQuantitiesDao partTableQuantitiesDao;


    @Override
    public void coverWjExcelImport(String id) {
        try {
            //导入文件路径
            String url = "F:\\吴江模板\\吴江表格模板\\上家结算汇总表（吴江）.xls";
            //创建文件流
            FileInputStream fileInputStream = new FileInputStream(new File(url));
            Sheet sheet = new Sheet(1, 1);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            List<Object> read = EasyExcelFactory.read(bufferedInputStream, sheet);

            LastSummaryCover lastSummaryCover = new LastSummaryCover();
            lastSummaryCover.setId(UUID.randomUUID().toString().replace("-", ""));
            lastSummaryCover.setConstructionUnit(((List<String>) read.get(0)).get(1));
            lastSummaryCover.setProjectNum(((List<String>) read.get(1)).get(1));
            lastSummaryCover.setNameProject(((List<String>) read.get(2)).get(1));
            lastSummaryCover.setConstructionOrganization(((List<String>) read.get(3)).get(1));
            lastSummaryCover.setBuildCost(((List<String>) read.get(4)).get(1));
            lastSummaryCover.setPreparePeople(((List<String>) read.get(5)).get(1));
            lastSummaryCover.setReviewer(((List<String>) read.get(5)).get(3));
            lastSummaryCover.setCompileTime(((List<String>) read.get(6)).get(1));
            lastSummaryCover.setDelFlag("0");
            System.out.println(lastSummaryCover);
            lastSummaryCoverDao.insertSelective(lastSummaryCover);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override //TODO 未写完
    public void summaryUnitsImport() {
        try {
            String url = "F:\\吴江模板\\吴江表格模板\\上家结算汇总表（吴江）.xls";
            FileInputStream fileInputStream = new FileInputStream(new File(url));
            //第二张表第4条数据开始
            Sheet sheet = new Sheet(2, 1);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            List<Object> read = EasyExcelFactory.read(bufferedInputStream, sheet);
            System.err.println(read);
            SummaryUnits summaryUnits = new SummaryUnits();
            System.err.println(read);
            System.err.println(read);
            System.err.println(read);
            System.err.println(read);
            String projectName = "";
            for (int i = 0; i < read.size(); i++) {
                if (i == 0) {
                    String s = ((List<String>) read.get(i)).get(0);
                    String[] split = s.split("：");
                    projectName = split[1];
                }
//                if (i == (read.size() - 2)) {
//                    summaryUnits.setTotalAmount(((List<String>) read.get(i)).get(4));
//                    break;
//                }
                if (i > 1) {
                    summaryUnits.setId(UUID.randomUUID().toString().replace("-", ""));
                    summaryUnits.setProjectName(projectName);
                    summaryUnits.setSerialNumber(((List<String>) read.get(i)).get(0));
                    summaryUnits.setAggregateContent(((List<String>) read.get(i)).get(1));
                    summaryUnits.setDelFlag("0");

//                    System.out.println(((List<String>) read.get(i)).get(2));
                    if (((List<String>) read.get(i)).get(4) != null && !"".equals(((List<String>) read.get(i)).get(4))) {
                        BigDecimal bigDecimal = new BigDecimal(((List<String>) read.get(i)).get(4));
                        summaryUnits.setAmount(bigDecimal);
                    }

                    System.out.println(((List<String>) read.get(i)));
                    System.out.println(((List<String>) read.get(i)).get(5));
                    if (((List<String>) read.get(i)).get(5) != null && !"".equals(((List<String>) read.get(i)).get(5))) {
                        BigDecimal bigDecimal1 = new BigDecimal(((List<String>) read.get(i)).get(5));
                        summaryUnits.setValuation(bigDecimal1);
                    }
                    summaryUnitsDao.insertSelective(summaryUnits);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override //TODO 未写完
    public void partTableQuantitiesImport(String id) {
        try {
            String url = "F:\\吴江模板\\吴江表格模板\\上家结算汇总表（吴江）.xls";
            FileInputStream fileInputStream = new FileInputStream(new File(url));
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            Sheet sheet = new Sheet(3, 1);
            List<Object> read = EasyExcelFactory.read(bufferedInputStream, sheet);
            PartTableQuantities partTableQuantities = new PartTableQuantities();
            for (int i = 0; i < read.size(); i++) {
                if (i == 0) {
                    String projectName = ((List<String>) read.get(i)).get(0);
                    String[] split = projectName.split("：");
                    projectName = split[1];
                    partTableQuantities.setId(UUID.randomUUID().toString().replace("-", ""));
                    partTableQuantities.setProjectName(projectName);
                }
                System.out.println(((List<String>) read.get(i)));
                if (i > 1) {
                    partTableQuantities.setSerialNumber(((List<String>) read.get(i)).get(0));
                    partTableQuantities.setProjectCode(((List<String>) read.get(i)).get(1));
                    partTableQuantities.setProjectName(((List<String>) read.get(i)).get(2));
                    partTableQuantities.setSignalment(((List<String>) read.get(i)).get(3));
                    partTableQuantities.setMeasurement(((List<String>) read.get(i)).get(5));
                    partTableQuantities.setEngineering(((List<String>) read.get(i)).get(6));
                    partTableQuantities.setDelFlag("0");
                    if (((List<String>) read.get(i)).get(8) != null && !"".equals(((List<String>) read.get(i)).get(8))) {
                        partTableQuantities.setComprehensivePrice(new BigDecimal(((List<String>) read.get(i)).get(8)));
                    }
                    if (((List<String>) read.get(i)).get(9) != null && !"".equals(((List<String>) read.get(i)).get(9))) {
                        partTableQuantities.setCombinedPrice(new BigDecimal(((List<String>) read.get(i)).get(10)));
                    }
                    if (((List<String>) read.get(i)).get(10) != null && !"".equals(((List<String>) read.get(i)).get(10))) {
                        partTableQuantities.setArtificialCost(new BigDecimal(((List<String>) read.get(i)).get(10)));
                    }
                    partTableQuantitiesDao.insertSelective(partTableQuantities);

                }
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
