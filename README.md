# 数据导出导入Excel封装工具

业务后台存在很多导出数据为Excel或者导入Excel文件进行数据处理的业务需求。
封装工具可使开发者更快捷的完成业务开发需求。

**使用 alibaba EasyExcel** 实现Sax模式写入与读取Excel，减少POI框架解析Excel文件过大时或并发量大时占用堆内存导致系统崩溃

#### 性能概况

使用100W两列xlsx文件测试。（test/resources/file/py-ts.xlsx）

1. POI解析Excel会把文件放入内存处理，在解析大文件容易造成堆溢出问题

![poi-parser](doc\img\poi-parser.png)

1. alibaba EasyExcel 解析Excel使用Sax流式处理，使用内存很小且处理速度非常快

![sax-parser](doc\img\sax-parser.png)

----

### 导出Excel
1.实现数据生产者
```
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
```

2.执行器调用

```
//异步导出
ExportExcelDataProperties properties = new ExportExcelDataProperties();
properties.setTmpFilePath("D:\\");//导出数据生成文件临时输出目录
AsyncDataExportExcelHandler exportExcelHandler = new AsyncDataExportExcelHandler(properties);
Map<String, Object> params = new HashMap<>();//运行参数
params.put(Constants.RUN_TYPE, Constants.RUN_TYPE_SAX);//默认使用POI解析框架，指定SAX模式解析Excel
String taskId = exportExcelHandler.startTask(new TestWriteDataProducer(), params);
String exportStatus = exportExcelHandler.getExportStatus(taskId);
while (!exportStatus.equals("-100") && Float.parseFloat(exportStatus) < 1){
    System.out.printf("process-status:%s \n", exportStatus);
    Thread.sleep(1000L);
    exportStatus = exportExcelHandler.getExportStatus(taskId);
}
System.out.printf("process-status:%s \n", exportStatus);
```

```
//同步导出
ExportExcelDataProperties properties = new ExportExcelDataProperties();
properties.setTmpFilePath("D:\\");//导出数据生成文件临时输出目录
DefaultDataExportExcelHandler exportExcelHandler = new SyncDataExportExcelHandler(properties);
params.put(Constants.RUN_TYPE, Constants.RUN_TYPE_SAX);//默认使用POI解析框架，指定SAX模式解析Excel
exportExcelHandler.handler(new TestWriteDataProducer(), params);
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
    
    public class Data {

        private String data;
        private boolean flag = false;

        public boolean isFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "data='" + data + '\'' +
                    ", flag=" + flag +
                    '}';
        }
    }
}
```

2.执行器调用

```
//同步执行
DataParserExcelHandler dataParserExcelHandler = new SyncDataParserExcelHandler();
Map<String,Object> params = new HashMap<>();//运行参数
params.put(Constants.RUN_TYPE, Constants.RUN_TYPE_SAX);//默认使用POI解析框架，指定SAX模式解析Excel
dataParserExcelHandler.readExcel("123", getResource("file/py-ts.xlsx"), new TestReadDataParser(), params);
System.out.println(dataParserExcelHandler.getDealStatus("123"));
```



```
//异步执行
AsyncDataParserExcelHandler asyncDataParserExcelHandler = new AsyncDataParserExcelHandler();
Map<String,Object> params = new HashMap<>();//运行参数
params.put(Constants.RUN_TYPE, Constants.RUN_TYPE_SAX);//默认使用POI解析框架，指定SAX模式解析Excel
String taskId = asyncDataParserExcelHandler.startTask(getResource("file/py-ts.xlsx"), new TestReadDataParser(), params);
DealStatus dealStatus = asyncDataParserExcelHandler.getDealStatus(taskId);
while (!dealStatus.isFinished() && !dealStatus.isException()){
    System.out.printf("process-status:%s \n", exportStatus.getStatus());
    Thread.sleep(1000L);
    dealStatus = asyncDataParserExcelHandler.getDealStatus(taskId);
}
System.out.printf("process-status:%s \n", dealStatus.getStatus());
```

#### 其他配置内容

1.导入

- 自定义类型转换逻辑

  ```
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
          //注册转换器到指定属性上
          registerCustomTransform("flag", new CustomBooleanFlagTransformate());
      }
  
  	//自定义类型转换器
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
  
  ```

- 导入POI模式，批量模式与一次模式

  ```
  DataParserExcelHandler dataParserExcelHandler = new SyncDataParserExcelHandler();
  Map<String,Object> params = new HashMap<>();
  //指定批量模式下处理数据量大小，默认为一次模式。使用批量模式，可以减少业务处理逻辑占用内存，加快内存的回收
  params.put(Constants.PARSE_BATCH_COUNT, Constants.PARSE_BATCH_DEFAULT_COUNT);
  dataParserExcelHandler.readExcel("123", getResource("file/py-ts.xlsx"), new TestReadDataParser(), params);
  System.out.println(dataParserExcelHandler.getDealStatus("123").getStatus());
  ```

- 获取异步导入处理进度

  ```
  DealStatus dealStatus = asyncDataParserExcelHandler.getDealStatus(taskId);
  //属性
  /*
  String status;//已处理数据量
  boolean finished;//任务是否完成
  boolean exception;//任务是否异常
  String errorMsg;//异常信息
  */
  ```

  

- 异步任务最大量设置

2.导出

- 导出文件位置设置

- 异步任务最大量设置

- 获取异步导出处理进度

  ```
  String exportStatus = exportExcelHandler.getExportStatus(taskId);
  //exportStatus为处理进度百分比数值
  //当数值为-100时，任务出现异常
  //获取异常信息
  String exportErrMsg = exportExcelHanlder.getErrorMsg(String taskUid);
  ```

  