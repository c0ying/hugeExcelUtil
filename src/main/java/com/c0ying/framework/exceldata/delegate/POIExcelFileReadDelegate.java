package com.c0ying.framework.exceldata.delegate;

import com.c0ying.framework.exceldata.HandlerWorkException;
import com.c0ying.framework.exceldata.imparse.ParseExcelException;
import com.c0ying.framework.exceldata.imparse.dataparser.SimpleExcelParser;
import com.c0ying.framework.exceldata.utils.Constants;
import com.c0ying.framework.exceldata.utils.ExcelReadUtil;
import com.c0ying.framework.exceldata.utils.ExcelReadUtil.ReadEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

public class POIExcelFileReadDelegate implements ExcelFileReadDelegater {

    private static Logger logger = LoggerFactory.getLogger(POIExcelFileReadDelegate.class);

    @Override
    public <T> void readExcel(String fileUrl, SimpleExcelParser<T> simpleExcelParser) throws Exception {
        try {
            if ((int) simpleExcelParser.getContext().getOrDefault(Constants.PARSE_BATCH_COUNT, 0) >0) {
                logger.info("Parse Excel in Normal mode and all once deal");
                List<List<String>> contentStrList =  ExcelReadUtil.readExcel(new File(fileUrl), true);
                if (contentStrList == null) {
                    throw new ParseExcelException("导入文件解析异常");
                }
                List<T> mT = simpleExcelParser.parse(contentStrList);
                simpleExcelParser.validate(mT);
                simpleExcelParser.process(mT);
            }else{
                logger.info("Parse Excel in Normal mode and partitioned data deal, partition of data num:{}",
                        simpleExcelParser.getContext().getOrDefault(Constants.PARSE_BATCH_COUNT, 0));
                ExcelReadUtil.readExcel(new File(fileUrl), true, new ReadEventListener() {
                    @Override
                    public void analysis(List<List<String>> contentStrList) throws Exception {
                        if (contentStrList == null) {
                            throw new ParseExcelException("导入文件解析异常");
                        }
                        List<T> mT = simpleExcelParser.parse(contentStrList);
                        simpleExcelParser.validate(mT);
                        simpleExcelParser.process(mT);
                    }
                    @Override
                    public int getBatchCount() {
                        return (int) simpleExcelParser.getContext().getOrDefault(Constants.PARSE_BATCH_COUNT, 0);
                    }
                });
            }
        } catch (Exception e) {
            if (e instanceof ParseExcelException){
                throw e;
            }else {
                throw new HandlerWorkException(e.getMessage(), e);
            }
        }finally {
            simpleExcelParser.destroy();
        }
    }

}
