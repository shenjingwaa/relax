<!-- docs/_annotaions.md 注释介绍-->


## @EnableRelax
开启Relax的功能
> @EnableRelax 使用示例
```java
@EnableRelax
@SpringBootApplication
public class RelaxApplication {
    public static void main(String[] args) {
        SpringApplication.run(RelaxApplication.class, args);
    }
}
```


## @RelaxClass
标记需要生成增删改查接口的Controller上
> @RelaxClass 使用示例
```java
// methods 属性表示需要生成的接口的类型,默认为下面6个接口,如果有接口不需要或者进行特殊业务单独写,则可以从中删除避免重复生成.
// entityType 属性中的类型要与 Controller 的业务对应

//此处会根据@RequestMapping中的前缀生成 /relaxTest/add,/relaxTest/delete,/relaxTest/update ...等接口
@RelaxClass(methods = {"add","delete","update","info","page","list"},entityType = RelaxTest.class)
@RestController
@RequestMapping("/relaxTest")
public class RelaxTestController {
    
}
```

## @RelaxEntity
标记需要反向生成表结构 或 标记增删改查入参以及返回值类
```java
@RelaxEntity(tableName = "relax_test")
public class RelaxTest {

    // 新增时可以设置不同的主键生成策略
    @RelaxId(IdType.SNOW_FLAKE)
    @RelaxColumn
    private Long id;

    // 与springboot校验包融合,增加不同分组的校验规则
    @NotNull(message = "新增数据时title字段不能为空",groups = ValidationGroup.Add.class)
    @RelaxColumn
    private String title;

    @NotNull(message = "修改数据时content1字段不能为空",groups = ValidationGroup.Update.class)
    @RelaxColumn
    private String content1;

    @RelaxColumn
    private Long content2;
    
    //此处省略get,set等方法

}


```

## @RelaxId
使用在被@RelaxEntity使用的类属性中,表示唯一标识字段
```java
@RelaxEntity(tableName = "relax_test")
public class RelaxTest {

    // 新增时可以设置不同的主键生成策略
    @RelaxId(IdType.SNOW_FLAKE)
    @RelaxColumn
    private Long id;

    @RelaxColumn
    private String title;
    
    @RelaxColumn
    private String content1;

    @RelaxColumn
    private Long content2;

    //此处省略get,set等方法
}
```

## @RelaxColumn
使用在被@RelaxEntity使用的类属性中,表示该字段可作为入参中的属性 和 返回结果中的属性
```java
@RelaxEntity(tableName = "relax_test")
public class RelaxTest {

    @RelaxId(IdType.SNOW_FLAKE)
    @RelaxColumn
    private Long id;

    // list,page接口查询时该字段进行' = '匹配
    @RelaxColumn(queryType = QueryType.ALL_LIKE)
    private String title;
    
    // list,page接口查询时该字段进行' %xxx% '匹配
    @RelaxColumn(queryType = QueryType.ALL_MATCH)
    private String content1;
    
    // list,page接口查询时该字段进行' %xxx '匹配
    @RelaxColumn(queryType = QueryType.LEFT_LIKE)
    private Long content2;

    //此处省略get,set等方法
}

```
