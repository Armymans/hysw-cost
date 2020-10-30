package net.zlw.cloud.excel.service.impl;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.metadata.Sheet;
import net.zlw.cloud.budgeting.service.BudgetingService;
import net.zlw.cloud.excel.dao.BudgetCoverDao;
import net.zlw.cloud.excel.service.BudgetCoverService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

@Service
@Transactional
public class BudgetCoverServiceimpl implements BudgetCoverService {
    @Resource
    private BudgetCoverDao budgetCoverDao;

    @Override
    public void coverImport() {
        String url = "E:\\正量\\新建文件夹\\预算汇总表-神机（安徽）.xlsx";
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(url));
            Sheet sheet = new Sheet(2,6);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            List<Object> read = EasyExcelFactory.read(bufferedInputStream, sheet);
            List<String> list =(List<String>) read.get(7);
            for (String textContent : list) {
                if (textContent!=null){
                    textContent = textContent.trim();
                    while (textContent.startsWith("　")) {//这里判断是不是全角空格
                        textContent = textContent.substring(1, textContent.length()).trim();
                    }
                    while (textContent.endsWith("　")) {
                        textContent = textContent.substring(0, textContent.length() - 1).trim();
                    }
                    System.out.println(textContent);
                }

            }
            for (Object o : read) {

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
