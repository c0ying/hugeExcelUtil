package com.c0ying.framework.exceldata.delegate;

public interface ExcelFileWriteDelegater {

    void init(String tempFile, String head, String delimiter);

    void writeData(String[] data, String delimiter);

    void finish();
}
