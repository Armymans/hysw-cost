package net.zlw.cloud.excel;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.metadata.Sheet;
import net.zlw.cloud.excel.model.BomTable;
import net.zlw.cloud.excel.model.BomTableInfomation;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;


public class Test01 {
    public static void main(String[] args) throws FileNotFoundException {
        String filePath = "E:\\正量\\新建文件夹\\物料清单表（安徽）.xls";
        //第几张表(最低1) 第几条数据开始(最低0)
        Sheet sheet = new Sheet(1, 1);
        FileInputStream fileInputStream = new FileInputStream(new File(filePath));
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        List<Object> read = EasyExcelFactory.read(bufferedInputStream, sheet);
        BomTableInfomation bomTableInfomation = new BomTableInfomation();
        bomTableInfomation.setId(UUID.randomUUID().toString().replace("-", ""));
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
            if (i >= 14) {
                if (compile.matcher(o1.get(0)).matches()) {
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
    }

}
