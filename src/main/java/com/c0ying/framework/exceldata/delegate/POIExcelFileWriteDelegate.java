package com.c0ying.framework.exceldata.delegate;

import com.c0ying.framework.exceldata.utils.ExcelWriteUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;

public class POIExcelFileWriteDelegate implements ExcelFileWriteDelegater {

    private SXSSFWorkbook workBook;
    private File wbFile;

    @Override
    public void init(String tempFile, String head, String delimiter) {
        workBook = ExcelWriteUtil.initExcel(head, delimiter);
        wbFile = new File(tempFile);
    }

    @Override
    public void writeData(String[] data, String delimiter) {
        ExcelWriteUtil.exportData(workBook, data, delimiter);
    }

    @Override
    public void finish() {
        try (FileOutputStream outputStream = new FileOutputStream(wbFile)){
            workBook.write(outputStream);
        }catch (Exception e) {
        }
    }
}
