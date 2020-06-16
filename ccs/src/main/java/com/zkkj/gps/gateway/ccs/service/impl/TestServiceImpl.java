package com.zkkj.gps.gateway.ccs.service.impl;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.zkkj.gps.gateway.ccs.dto.excel.ExcelDataDto;
import com.zkkj.gps.gateway.ccs.entity.test.PointSource;
import com.zkkj.gps.gateway.ccs.exception.ParamException;
import com.zkkj.gps.gateway.ccs.service.GpsInternalService;
import com.zkkj.gps.gateway.ccs.service.TestService;
import com.zkkj.gps.gateway.ccs.utils.BeanValidate;
import com.zkkj.gps.gateway.ccs.utils.ExcelUtil;
import com.zkkj.gps.gateway.common.utils.DateTimeUtils;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BaseGPSPositionDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BasicPositionDto;

/**
 * author : cyc
 * Date : 2020/3/16
 */

@Service
public class TestServiceImpl implements TestService {


    @Autowired
    private GpsInternalService gpsInternalService;

    @Override
    public void exportPositionList(PointSource pointSource) throws Exception {
        BeanValidate.checkParam(pointSource);
        ExcelDataDto excelDataDto = getExcelDataDto(pointSource);
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        ExcelUtil.export(excelDataDto, response);
    }

    @Override
    public void importPositionList(MultipartFile file, String dispatchNo) throws Exception {
        //获取excel中的数据
        List<BaseGPSPositionDto> gpsPositionDtoList = getExcelPositionList(file);
        for (BaseGPSPositionDto baseGPSPositionDto : gpsPositionDtoList) {
            Thread.sleep(500);
            gpsInternalService.positionChange(baseGPSPositionDto.getTerminalId(), baseGPSPositionDto, dispatchNo);
        }
    }

    /**
     * 获取excel的点位数据
     * @param file
     * @return
     * @throws Exception
     */
    private List<BaseGPSPositionDto> getExcelPositionList(MultipartFile file) throws Exception {
        Workbook workbook;
        InputStream in = file.getInputStream();
        //导入的所有员工集合
        List<BaseGPSPositionDto> baseGPSPositionDtos = Lists.newArrayList();
        if (file.getOriginalFilename().endsWith(".xls")) {
            workbook = new HSSFWorkbook(in);
        } else if (file.getOriginalFilename().endsWith(".xlsx")) {
            workbook = new XSSFWorkbook(in);
        } else {
            throw new ParamException("文件格式不正确");
        }
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        Sheet sheet0 = workbook.getSheetAt(0);
        int lastRowNum = sheet0.getLastRowNum();
        //从第2行开始
        for (int i = 2; i <= lastRowNum; i++) {
            Row row = sheet0.getRow(i);
            if (row == null) {
                return null;
            }
            BaseGPSPositionDto baseGPSPositionDto = new BaseGPSPositionDto();
            BasicPositionDto positionDto = new BasicPositionDto();
            short lastCellNum = row.getLastCellNum();
            for (int j = 0; j < lastCellNum; j++) {
                Cell cell = row.getCell(j);
                setValueToModel(cell, j, baseGPSPositionDto, positionDto, evaluator);
                baseGPSPositionDto.setPoint(positionDto);
            }
            baseGPSPositionDtos.add(baseGPSPositionDto);
        }
        return baseGPSPositionDtos;
    }

    /**
     * 从单元格中获取值封装到点位对象中
     * @param cell
     * @param j
     * @param baseGPSPositionDto
     * @param basicPositionDto
     * @param evaluator
     */
    private void setValueToModel(Cell cell, int j, BaseGPSPositionDto baseGPSPositionDto, BasicPositionDto basicPositionDto, FormulaEvaluator evaluator) {
        String value = ExcelUtil.getCellValueByCell(cell, evaluator);
        switch (j) {
            case 0:
                baseGPSPositionDto.setTerminalId(value);
                break;
            case 1:
                basicPositionDto.setDate(DateTimeUtils.parseLocalDateTime(value));
                baseGPSPositionDto.setPoint(basicPositionDto);
                break;
            case 2:
                baseGPSPositionDto.setRcvTime(DateTimeUtils.parseLocalDateTime(value));
                break;
            case 3:
                basicPositionDto.setSpeed(Integer.valueOf(value));
                break;
            case 4:
                basicPositionDto.setLongitude(Double.valueOf(value));
                break;
            case 5:
                basicPositionDto.setLatitude(Double.valueOf(value));
                break;
            case 6:
                baseGPSPositionDto.setFlag(Integer.valueOf(value));
        }
    }



    /**
     * 获取导入excel模型
     *
     * @param pointSource
     * @return
     */
    private ExcelDataDto getExcelDataDto(PointSource pointSource) {
        List<List<Object>> rowsDataList = getRowsDataList(pointSource);
        List<String> titles = getTitles();
        return ExcelDataDto.builder().rowsData(rowsDataList).sheetName("数据源").titles(titles).build();
    }

    private List<String> getTitles() {
        return Lists.newArrayList("设备号", "gps时间(yyyy-MM-dd HH:mm:ss)", "rcv时间(yyyy-MM-dd HH:mm:ss)", "速度(百米/小时)", "经度", "纬度","设备类型");
    }

    private List<List<Object>> getRowsDataList(PointSource pointSource) {
        String[] pointArr = pointSource.getLineStrings().split(";");
        if (pointArr == null || pointArr.length <= 0) {
            return null;
        }
        List<List<Object>> rowsDataList = Lists.newArrayList();
        LocalDateTime currentLocalDateTime = DateTimeUtils.getCurrentLocalDateTime();
        for (String pointStr : pointArr) {
            String[] split = pointStr.split(",");
            if (split == null || split.length <= 0) {
                continue;
            }
            List<Object> rowData = Lists.newArrayList();
            rowData.add(pointSource.getTerminalId());
            rowData.add(DateTimeUtils.formatLocalDateTime(currentLocalDateTime));
            rowData.add(DateTimeUtils.formatLocalDateTime(currentLocalDateTime));
            rowData.add(0);
            rowData.add(split[1]);
            rowData.add(split[0]);
            rowData.add(pointSource.getType());
            rowsDataList.add(rowData);
            currentLocalDateTime = currentLocalDateTime.plusSeconds(20);
        }
        return rowsDataList;
    }
}
