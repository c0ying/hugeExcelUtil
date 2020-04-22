package com.c0ying.framework.exceldata.delegate;

import com.alibaba.excel.EasyExcel;
import com.c0ying.framework.exceldata.imparse.SimpleExcelParser;
import com.c0ying.framework.exceldata.imparse.StreamParseDataListener;
import com.c0ying.framework.exceldata.utils.ExcelReadUtil;

import java.io.File;
import java.util.List;

public class ExcelFileReadDelegate implements ExcelFileReadDelegater {

    @Override
    public List<List<String>> readExcel(String fileUrl, boolean skipHead) {
        return ExcelReadUtil.readExcel(new File(fileUrl), true);
    }

    @Override
    public void saxReadExcel(String fileUrl, SimpleExcelParser simpleExcelParser) {
        EasyExcel.read(fileUrl, new StreamParseDataListener(simpleExcelParser, true)).sheet().doRead();
    }
}
