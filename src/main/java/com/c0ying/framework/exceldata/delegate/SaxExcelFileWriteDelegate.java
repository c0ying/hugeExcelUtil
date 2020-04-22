package com.c0ying.framework.exceldata.delegate;

import com.c0ying.framework.exceldata.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SaxExcelFileWriteDelegate implements ExcelFileWriteDelegater {

    private SaxExcelWriteHandler saxExcelWriteHandler;

    @Override
    public void init(String tempFile, String head, String delimiter) {
        this.saxExcelWriteHandler = SaxExcelWriteHandler.initWriteExcel(tempFile, StringUtils.tokenizer(head, delimiter));
    }

    @Override
    public void writeData(String[] data, String delimiter) {
        List<List<String>> all = new ArrayList<>();
        for (String content : data) {
            String[] tokenizer = StringUtils.tokenizer(content, delimiter);
            all.add(Arrays.asList(tokenizer));
        }
        saxExcelWriteHandler.writeExcelData(all);
    }

    @Override
    public void finish() {
        saxExcelWriteHandler.finish();
    }
}
