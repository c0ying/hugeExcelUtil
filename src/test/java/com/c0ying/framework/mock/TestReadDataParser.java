package com.c0ying.framework.mock;

import com.c0ying.framework.exceldata.imparse.dataparser.CustomTransformBean;
import com.c0ying.framework.exceldata.imparse.dataparser.SimpleExcelParser;
import com.c0ying.framework.exceldata.imparse.ParseExcelException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TestReadDataParser extends SimpleExcelParser<Data> {

    public TestReadDataParser() {
        super(Arrays.asList("data","flag"));
    }

    @Override
    public List<Data> process(List<Data> mT) throws ParseExcelException {
        System.out.println(mT.size());
//        mT.forEach(m -> System.out.println(m.toString()));
        return mT;
    }

    @Override
    protected void init(Map<String, Object> context) {
        super.init(context);
        registerCustomTransform("flag", new CustomBooleanFlagTransformate());
    }

    class CustomBooleanFlagTransformate implements CustomTransformBean<Data>{

        @Override
        public void customTransform(Data obj, String value, int index, List<String> fieldsMapping) {
            if (index == 1){
                if (value.trim().equals("是")){
                    obj.setFlag(true);
                }else{
                    obj.setFlag(false);
                }
            }
        }
    }
}
