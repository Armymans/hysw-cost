package net.zlw.cloud.excel.Listener;

import com.alibaba.excel.event.WriteHandler;
import org.apache.poi.ss.usermodel.*;
public class StyleExcelHandler implements WriteHandler {

    // 把样式提出来防止重复new
    private CellStyle cellStyle;

    @Override
    public void sheet(int i, Sheet sheet) {
        Workbook workbook = sheet.getWorkbook();
        CellStyle cellStyle = workbook.createCellStyle();
        this.cellStyle = createStyle(cellStyle);
    }

    @Override
    public void row(int i, Row row) {
    }

    @Override
    public void cell(int i, Cell cell) {
        // 从第二行开始设置格式，第一行是表头 从0行开始
        if (cell.getRowIndex() > 0) {
            cell.getRow().getCell(i).setCellStyle(cellStyle);
        }
    }

    /**
     * 实际中如果直接获取原单元格的样式进行修改,
     * 最后发现是改了整行的样式, 因此这里是新建一个样式
     */
    private CellStyle createStyle(CellStyle cellStyle) {
        // 下边框
        cellStyle.setBorderBottom(BorderStyle.THIN);
        // 左边框
        cellStyle.setBorderLeft(BorderStyle.THIN);
        // 上边框
        cellStyle.setBorderTop(BorderStyle.THIN);
        // 右边框
        cellStyle.setBorderRight(BorderStyle.THIN);
        // 水平对齐方式
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        // 垂直对齐方式
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return cellStyle;
    }
}
