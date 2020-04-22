package com.c0ying.framework.exceldata.imparse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractImportExcelHandler<T> {

    private Logger logger = LoggerFactory.getLogger(SimpleExcelParser.class);

    protected SimpleExcelParser<T> parser;
    protected String fileUrl;
    protected Map<String, Object> context = new HashMap<>();

    public AbstractImportExcelHandler(SimpleExcelParser<T> parser, String fileUrl) {
        this.parser = parser;
        this.fileUrl = fileUrl;
    }

    public abstract List<T> handler() throws Exception ;

    protected List<T> processInternal(List<List<String>> contentStrList) throws Exception{
        List<T> mT = parser.parse(contentStrList);
        parser.validate(mT);
        return parser.process(mT);
    }

    public void setContext(Map<String, Object> context) {
        this.context = context;
    }
}
