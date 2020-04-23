package com.c0ying.framework.exceldata.imparse;

import com.c0ying.framework.exceldata.imparse.bean.ParseExcelException;

import java.util.List;

public class DefaultExcelParser<T> extends SimpleExcelParser<T> {

    public DefaultExcelParser(ImportProcesser<T> importProcesser, List<String> mappings) {
        super(importProcesser, mappings);
    }

    @Override
    public List<T> process(List<T> mT) throws ParseExcelException {
        if (importProcesser != null) {
            return importProcesser.process(mT);
        }
        return null;
    }
}
