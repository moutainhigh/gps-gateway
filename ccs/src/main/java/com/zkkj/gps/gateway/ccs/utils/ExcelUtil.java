package com.zkkj.gps.gateway.ccs.utils;


import com.zkkj.gps.gateway.ccs.dto.excel.ExcelDataDto;
import com.zkkj.gps.gateway.ccs.exception.ParamException;
import com.zkkj.gps.gateway.common.utils.DateTimeUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.List;

/**
 * author : cyc
 * Date : 2018-09-19
 */
public class ExcelUtil {

    public static void export(ExcelDataDto excelDataDto, HttpServletResponse response) throws Exception {
        OutputStream out = null;
        try {
            HSSFWorkbook workbook = new HSSFWorkbook(); // 创建一个excel对象
            HSSFSheet sheet = workbook.createSheet(excelDataDto.getSheetName()); // 创建表格
            // 产生表格标题行
            HSSFRow rowm = sheet.createRow(0); // 行
            HSSFCell cellTiltle = rowm.createCell(0); // 单元格
            // sheet样式定义
            HSSFCellStyle columnTopStyle = getColumnTopStyle(workbook); // 头样式
            HSSFCellStyle style = getStyle(workbook); // 单元格样式
            /**
             * 参数说明 从0开始 第一行 第一列 都是从角标0开始 行 列 行列 (0,0,0,5) 合并第一行 第一列 到第一行 第六列
             * 起始行，起始列，结束行，结束列
             *
             * new Region() 这个方法使过时的
             */
            HSSFFont headfont = workbook.createFont();
            headfont.setFontName("宋体");
            headfont.setFontHeightInPoints((short) 22);// 字体大小
            HSSFCellStyle headstyle = workbook.createCellStyle();
            headstyle.setFont(headfont);
            headstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
            headstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
            headstyle.setLocked(true);
            // 合并第一行的所有列
            sheet.addMergedRegion(new CellRangeAddress(0, (short) 0, 0, (short) (excelDataDto.getTitles().size() - 1)));
            cellTiltle.setCellStyle(headstyle);
            cellTiltle.setCellValue(excelDataDto.getSheetName());
            int columnNum = excelDataDto.getTitles().size(); // 表格列的长度
            HSSFRow rowRowName = sheet.createRow(1); // 在第二行创建行
            HSSFCellStyle cells = workbook.createCellStyle();
            cells.setBottomBorderColor(HSSFColor.BLACK.index);
            rowRowName.setRowStyle(cells);

            // 循环 将列名放进去
            for (int i = 0; i < columnNum; i++) {
                HSSFCell cellRowName = rowRowName.createCell((int) i);
                cellRowName.setCellType(HSSFCell.CELL_TYPE_STRING); // 单元格类型
                HSSFRichTextString text = new HSSFRichTextString(excelDataDto.getTitles().get(i)); // 得到列的值
                cellRowName.setCellValue(text); // 设置列的值
                cellRowName.setCellStyle(columnTopStyle); // 样式
            }

            // 将查询到的数据设置到对应的单元格中
            for (int i = 0; i < excelDataDto.getRowsData().size(); i++) {
                List<Object> obj = excelDataDto.getRowsData().get(i);// 遍历每个对象
                HSSFRow row = sheet.createRow(i + 2);// 创建所需的行数
                for (int j = 0; j < obj.size(); j++) {
                    HSSFCell cell = null; // 设置单元格的数据类型
                    /*if (j == 0) {
                        // 第一列设置为序号，替换第一列的主键
                        cell = row.createCell(j, HSSFCell.CELL_TYPE_NUMERIC);
                        cell.setCellValue(i + 1);
                    } else {
                        cell = row.createCell(j, HSSFCell.CELL_TYPE_STRING);
                        if (!"".equals(obj.get(j)) && obj.get(j) != null) {
                            cell.setCellValue(obj.get(j).toString()); // 设置单元格的值
                        } else {
                            cell.setCellValue("  ");
                        }
                    }*/
                    cell = row.createCell(j, HSSFCell.CELL_TYPE_STRING);
                    if (!"".equals(obj.get(j)) && obj.get(j) != null) {
                        cell.setCellValue(obj.get(j).toString()); // 设置单元格的值
                    } else {
                        cell.setCellValue("  ");
                    }
                    cell.setCellStyle(style); // 样式
                }
            }
            // 让列宽随着导出的列长自动适应
            sheet.setColumnWidth(0, 256 * 20 + 184);
            sheet.setColumnWidth(1, 256 * 40 + 184);
            sheet.setColumnWidth(2, 256 * 40 + 184);
            sheet.setColumnWidth(3, 256 * 15 + 184);
            sheet.setColumnWidth(4, 256 * 20 + 184);
            sheet.setColumnWidth(5, 256 * 20 + 184);
            sheet.setColumnWidth(5, 256 * 15 + 184);
            if (workbook != null) {
                // excel 表文件名
                String fileName = excelDataDto.getSheetName() + DateTimeUtils.getCurrentDateTimeStr("yyyyMMddHHmmss") + ".xls";
                String fileName11 = URLEncoder.encode(fileName, "UTF-8");
                String headStr = "attachment; filename=\"" + fileName11 + "\"";
                response.setContentType("APPLICATION/OCTET-STREAM");
                response.setHeader("Content-Disposition", headStr);
                out = response.getOutputStream();
                workbook.write(out);
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ParamException("导出失败");
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (Exception e) {
                    throw new ParamException("导出失败");
                }
            }
        }
    }

    public static HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {

        // 设置字体
        HSSFFont font = workbook.createFont();
        // 设置字体大小
        font.setFontHeightInPoints((short) 11);
        // 字体加粗
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 设置字体名字
        font.setFontName("Courier New");
        // 设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置底边框;
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        // 设置底边框颜色;
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        // 设置左边框;
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        // 设置左边框颜色;
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        // 设置右边框;
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        // 设置右边框颜色;
        style.setRightBorderColor(HSSFColor.BLACK.index);
        // 设置顶边框;
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        // 设置顶边框颜色;
        style.setTopBorderColor(HSSFColor.BLACK.index);
        // 在样式用应用设置的字体;
        style.setFont(font);
        // 设置自动换行;
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        return style;

    }

    public static HSSFCellStyle getStyle(HSSFWorkbook workbook) {
        // 设置字体
        HSSFFont font = workbook.createFont();
        // 设置字体大小
        // font.setFontHeightInPoints((short)10);
        // 字体加粗
        // font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 设置字体名字
        font.setFontName("Courier New");
        // 设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置底边框;
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        // 设置底边框颜色;
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        // 设置左边框;
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        // 设置左边框颜色;
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        // 设置右边框;
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        // 设置右边框颜色;
        style.setRightBorderColor(HSSFColor.BLACK.index);
        // 设置顶边框;
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        // 设置顶边框颜色;
        style.setTopBorderColor(HSSFColor.BLACK.index);
        // 在样式用应用设置的字体;
        style.setFont(font);
        // 设置自动换行;
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        return style;
    }

    /**
     * 获取单元格各类型值，返回字符串类型
     *
     * @param cell
     * @param evaluator
     * @return
     */
    public static String getCellValueByCell(Cell cell, FormulaEvaluator evaluator) {
        // 判断是否为null或空串
        if (cell == null || cell.toString().trim().equals("")) {
            return "";
        }
        String cellValue = "";
        int cellType = cell.getCellType();
        if (cellType == Cell.CELL_TYPE_FORMULA) { // 表达式类型
            cellType = evaluator.evaluate(cell).getCellType();
        }
        switch (cellType) {
            case Cell.CELL_TYPE_STRING: // 字符串类型
                cellValue = cell.getStringCellValue().trim();
                cellValue = com.alibaba.druid.util.StringUtils.isEmpty(cellValue) ? "" : cellValue;
                break;
            case Cell.CELL_TYPE_BOOLEAN: // 布尔类型
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_NUMERIC: // 数值类型
                if (HSSFDateUtil.isCellDateFormatted(cell)) { // 判断日期类型
                    //cellValue = DateFormatUtil.formatDate(cell.getDateCellValue());
                } else { // 否
                    cellValue = new DecimalFormat("#.######").format(cell.getNumericCellValue());
                }
                break;
            default: // 其它类型，取空串吧
                cellValue = "";
                break;
        }
        return cellValue;
    }

}
