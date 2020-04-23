package com.c0ying.framework.mock;

import com.c0ying.framework.exceldata.export.dataproducer.SimpleDataAsyncProducer;

import java.util.ArrayList;
import java.util.List;

public class TestWriteDataProducer extends SimpleDataAsyncProducer<List<String>> {

    private int page = 0;

    @Override
    public boolean hasMore() {
        return getCurrent() <= getTotal();
    }

    @Override
    public long getTotal() {
        return 100;
    }

    @Override
    public long getCurrent() {
        return page;
    }

    @Override
    public List<String> next() {
        page++;
        return null;
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
