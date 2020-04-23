package com.c0ying.framework.exceldata.delegate;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
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

    static class SaxExcelWriteHandler {

        private ExcelWriter excelWriter;
        private WriteSheet writeSheet;

        private SaxExcelWriteHandler(ExcelWriter excelWriter){
            this.excelWriter = excelWriter;
            this.writeSheet = EasyExcel.writerSheet("sheet1").build();
        }

        public void writeExcelData(List data){
            excelWriter.write(data, writeSheet);
        }

        public void finish(){
            excelWriter.finish();
        }

        public static SaxExcelWriteHandler initWriteExcel(String tempFile, String[] head){
            SaxExcelWriteHandler saxExcelWriteHandler = new SaxExcelWriteHandler(EasyExcel.write(tempFile).build());
            saxExcelWriteHandler.writeExcelData(Arrays.asList(head));
            return saxExcelWriteHandler;
        }
    }
}
