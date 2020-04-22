package com.c0ying.framework.exceldata.delegate;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;

import java.util.Arrays;
import java.util.List;

public class SaxExcelWriteHandler {

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
