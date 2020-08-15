package com.c0ying.framework.exceldata.delegate;

import com.c0ying.framework.exceldata.utils.Constants;

public class ExcelFileWriteDelegate implements ExcelFileWriteDelegater {

    private ExcelFileWriteDelegate(){
    }

    @Override
    public void init(String tempFile, String head, String delimiter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeData(String[] data, String delimiter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void finish() {
        throw new UnsupportedOperationException();
    }

    public static ExcelFileWriteDelegater build(String runType){
        if (Constants.RUN_TYPE_NORMAL.equalsIgnoreCase(runType)){
            return new POIExcelFileWriteDelegate();
        }else{
            return new SaxExcelFileWriteDelegate();
        }
    }
}
