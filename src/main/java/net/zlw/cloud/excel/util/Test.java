package net.zlw.cloud.excel.util;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.read.metadata.ReadWorkbook;

import java.io.File;
import java.util.List;

/**
 * @Classname Test
 * @Description TODO
 * @Date 2020/11/12 15:20
 * @Created by sjf
 */
public class Test {
    public static void main(String[] args) {


//            String url = "F:\\吴江模板\\excel\\hello.xls";
//            FileInputStream fileInputStream = new FileInputStream(new File(url));
            ReadWorkbook readWorkbook = new ReadWorkbook();
            readWorkbook.setFile(new File("F:\\吴江模板\\excel\\hello.xls"));
            List<ReadSheet> readSheets = new ExcelReader(readWorkbook).excelExecutor().sheetList();

        readWorkbook.getAutoCloseStream();
        readWorkbook.getAutoCloseStream();

        for (ReadSheet readSheet : readSheets) {
                System.out.println(readSheet.getSheetName()+" --- " +readSheet.getSheetNo());

            }
        System.out.println(readSheets.size());



    }

}
