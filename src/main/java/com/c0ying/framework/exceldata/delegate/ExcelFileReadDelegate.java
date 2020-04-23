package com.c0ying.framework.exceldata.delegate;

import com.alibaba.excel.EasyExcel;
import com.c0ying.framework.exceldata.imparse.DefaultExcelParser;
import com.c0ying.framework.exceldata.imparse.ImportProcesser;
import com.c0ying.framework.exceldata.imparse.SimpleExcelParser;
import com.c0ying.framework.exceldata.imparse.StreamParseDataListener;
import com.c0ying.framework.exceldata.imparse.bean.ParseExcelException;
import com.c0ying.framework.exceldata.utils.ExcelReadUtil;

import java.io.File;
import java.util.List;

public class ExcelFileReadDelegate implements ExcelFileReadDelegater {

    public<T> void readExcel(String fileUrl, SimpleExcelParser<T> simpleExcelParser) throws Exception {
        Object runType = simpleExcelParser.getContext().getOrDefault("runType", "normal");
        if (runType.equals("normal")){
            try {
                List<List<String>> contentStrList =  ExcelReadUtil.readExcel(new File(fileUrl), true);;
                if (contentStrList == null) {
                    throw new ParseExcelException("导入文件解析异常");
                }
                List<T> mT = simpleExcelParser.parse(contentStrList);
                simpleExcelParser.validate(mT);
                simpleExcelParser.process(mT);
            } catch (Exception e) {
                throw new ParseExcelException(e.getMessage(), e);
            }finally {
                simpleExcelParser.destroy();
            }
        }else{
            EasyExcel.read(fileUrl, new StreamParseDataListener(simpleExcelParser, true)).sheet().doRead();
        }
    }

    public<T> void readExcel(String fileUrl, ImportProcesser<T> importProcesser) throws Exception {
        readExcel(fileUrl, new DefaultExcelParser<>(importProcesser, (List<String>) importProcesser.getContext().get("filedMappings")));
    }
}
