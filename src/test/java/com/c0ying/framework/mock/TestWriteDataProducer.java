package com.c0ying.framework.mock;

import com.c0ying.framework.exceldata.export.dataproducer.SimpleDataProducer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestWriteDataProducer extends SimpleDataProducer<List<String>> {

    @Override
    public void init(Map<String, Object> param) {
        super.init(param);
        setTotal(100);
    }

    @Override
    public List<String> data() {
        List<String> data = new ArrayList<>();
        data.add("testString");
        return data;
    }

    @Override
    public String[] formate(List<String> data) {
        return data.toArray(new String[0]);
    }


}
