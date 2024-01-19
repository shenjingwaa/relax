<p align="center">
	<img alt="logo" src="https://st-gtmc.oss-cn-hangzhou.aliyuncs.com/MiniProgram/EKZLms3kxzbhSKRelax.png">
</p>
<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">Relax</h1>
<h4 align="center">基于SpringBoot的快速CRUD工具。</h4>
<p align="center">
	<a href="https://github.com/shenjingwaa/relax"><img src="https://badgen.net/github/release/shenjingwaa/relax/stable"></a>
    	<a href="https://shenjingwaa.github.io/relax/#/"><img src="https://img.shields.io/badge/relax-page-garee"></a>
	<a href="https://github.com/shenjingwaa/relax/blob/master/License"><img src="https://img.shields.io/github/license/mashape/apistatus.svg"></a>
</p>

## 快速开始
Relax对API接口中的CRUD（创建、读取、更新、删除）场景进行了丰富的封装，大幅减少了开发人员在这方面的编码工作。
### 引入
```java
<dependency>
    <groupId>io.github.shenjingwaa</groupId>
    <artifactId>relax-spring-boot-starter</artifactId>
    <version>${latest-version}</version>
</dependency>
```
### 开启
```java
@EnableRelax
@SpringBootApplication
public class RelaxApplication {
    public static void main(String[] args) {
        SpringApplication.run(RelaxApplication.class, args);
    }
}
```

### 配置
```yaml
# 当前配置文件为 application.yml 文件
relax:
    auto-create-table: true  # 开启自动表创建
    entity-locations: com.app.entity.**  # relax实体扫描位置(默认为springboot启动类所在文件夹及子文件夹)
```

> 注:
>
> 如果需要开启自动表创建,需在application配置文件中增加配置relax.auto-create-table: true
>
> 该配置默认关闭,建议在dev环境开启,prod环境关闭该功能!

### 创建表 & 参数格式 & 返回值格式
```java
//关于@RelaxEntity,如果您的auto-create-table配置为true,那么将会自动创建表.
//为false,则只会用于接口的生成
@RelaxEntity(tableName = "relax_user") 
public class RelaxUser {

    // 必须有且仅有一个属性通过 @RelaxId 显示指定表的主键,
    // 自动创建表字段会根据 @RelaxColumn 标注的属性,并且能够作为增删改查条件使用.

    @RelaxId
    @RelaxColumn
    private Long id;

    @RelaxColumn
    private String username;
    
    @RelaxColumn
    private String avatar;

    @RelaxColumn
    private LocalDate birthday;

    @RelaxColumn
    private Integer userType;

    @RelaxColumn
    private Integer status;
    
    // 此处省略get,set等方法...

}
```
### 创建API接口
```java
@RelaxClass(entityType = RelaxUser.class)
@RestController
@RequestMapping("/relaxUser")
public class RelaxUserController {

}
```

> 此时您已经成功创建了接口创建,下面即可进入测试步骤


### 使用接口
根据以上示例生成的接口应为:
#### 新增数据
POST /relaxUser/add

| 字段名 | 字段值 | 所在位置 | 是否必填 |
| --- | --- | --- | --- |
| id | 1 | RequestBody | 是 |
| username | sili | RequestBody | 否 |
| avatar | [https://avatars.githubusercontent.com/u/112607471?v=4](https://avatars.githubusercontent.com/u/112607471?v=4) | RequestBody | 否 |
| birthday | 1989-03-27  | RequestBody | 否 |
| userType | 00 | RequestBody | 否 |
| status | 0 | RequestBody | 否 |

#### 更新数据
POST /relaxUser/update

| 字段名 | 字段值 | 所在位置 | 是否必填 |
| --- | --- | --- | --- |
| id | 1 | RequestBody | 是 |
| username | sili2 | RequestBody | 否 |
| avatar | [https://avatars.githubusercontent.com/u/112607471?v=4](https://avatars.githubusercontent.com/u/112607471?v=4) | RequestBody | 否 |
| birthday | 1989-03-27  | RequestBody | 否 |
| userType | 00 | RequestBody | 否 |
| status | 0 | RequestBody | 否 |

#### 删除数据
POST /relaxUser/delete

| 字段名 | 字段值 | 所在位置 | 是否必填 |
| --- | --- | --- | --- |
| id | 1 | RequestBody | 是 |

#### 根据ID查看详情
GET /relaxUser/info

| 字段名 | 字段值 | 所在位置 | 是否必填 |
| --- | --- | --- | --- |
| id | 1 | param | 是 |

#### 列表查询
POST /relaxUser/list

| 字段名 | 字段值 | 所在位置 | 是否必填 |
| --- | --- | --- | --- |
| id | 1 | RequestBody | 否 |
| username | sili | RequestBody | 否 |
| avatar | [https://avatars.githubusercontent.com/u/112607471?v=4](https://avatars.githubusercontent.com/u/112607471?v=4) | RequestBody | 否 |
| birthday | 1989-03-27  | RequestBody | 否 |
| userType | 00 | RequestBody | 否 |
| status | 0 | RequestBody | 否 |


#### 分页查询
POST /relaxUser/page

| 字段名 | 字段值 | 所在位置 | 是否必填 |
| --- | --- | --- | --- |
| id | 1 | RequestBody | 否 |
| username | sili | RequestBody | 否 |
| avatar | [https://avatars.githubusercontent.com/u/112607471?v=4](https://avatars.githubusercontent.com/u/112607471?v=4) | RequestBody | 否 |
| birthday | 1989-03-27  | RequestBody | 否 |
| userType | 00 | RequestBody | 否 |
| status | 0 | RequestBody | 否 |
| pageSize | 1 | param | 否(默认1) |
| pageNum | 10 | param | 否(默认10) |


