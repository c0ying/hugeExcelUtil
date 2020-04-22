package com.c0ying.framework.mock;

import com.c0ying.framework.exceldata.imparse.SimpleExcelParser;
import com.c0ying.framework.exceldata.imparse.bean.ParseExcelException;

import java.util.Arrays;
import java.util.List;

public class TestReadDataParser extends SimpleExcelParser<Data> {

    public TestReadDataParser() {
        super(Arrays.asList("data"));
    }

    @Override
    public List<Data> process(List<Data> mT) throws ParseExcelException {
        System.out.println(mT.size());
        return mT;
    }
}
