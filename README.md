# 数据导出Excel封装工具

业务后台存在很多导出数据为Excel或者导入Excel文件进行数据处理的业务需求。
封装工具可使开发者更快捷的完成业务开发需求。

**使用 alibaba EasyExcel** 实现Sax模式写入与读取Excel，减少POI框架解析Excel过度占用堆内存导致系统崩溃

### 导出Excel
1.实现数据生产者
```
public class TestWriteDataProducer extends SimpleDataAsyncProducer<List<String>> {

    private int page = 1;

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

    //返回当前页数据
    @Override
    public List<String> data() {
        List<String> data = new ArrayList<>();
        data.add("testString");
        return data;
    }

    //转换数据为字符串格式
    @Override
    public String[] formate(List<String> data) {
        return data.toArray(new String[0]);
    }
}

```

2.执行器调用

```
AsyncDataExportExcelHandler exportExcelHandler = new AsyncDataExportExcelHandler();
Map<String, Object> params = new HashMap<>();
params.put(Constants.RUN_TYPE, Constants.RUN_TYPE_SAX);
String taskId = exportExcelHandler.startTask(new TestWriteDataProducer(), params);
String exportStatus = exportExcelHandler.getExportStatus(taskId);
while (!exportStatus.equals("-100") && Float.parseFloat(exportStatus) < 1){
    System.out.printf("process-status:%s \n", exportStatus);
    Thread.sleep(1000L);
    exportStatus = exportExcelHandler.getExportStatus(taskId);
}
System.out.printf("process-status:%s \n", exportStatus);
```

### 导入Excel

1.实现数据解析者。把数据转换成对应的Bean

```
public class TestReadDataParser extends SimpleExcelParser<Data> {

    public TestReadDataParser() {
        super(Arrays.asList("data"));
    }

    @Override
    public List<Data> process(List<Data> mT) throws ParseExcelException {
        System.out.println(mT.size());
        mT.forEach(m -> System.out.println(m.toString()));
        return mT;
    }
}
```

2.执行器调用

```
ExcelFileReadDelegate excelFileReadDelegate = new ExcelFileReadDelegate();
try {
    TestReadDataParser testReadDataParser = new TestReadDataParser();
    testReadDataParser.getContext().put("runType", "sax");
    excelFileReadDelegate.readExcel(getResource("file/10.xlsx"), testReadDataParser);
} catch (Exception e) {
    e.printStackTrace();
}
```
