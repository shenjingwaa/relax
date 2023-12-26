# Relax
基于SpringBoot的快速通用开发包
## 使用
### 引入
```java
<dependency>
<groupId>io.github.shenjingwaa</groupId>
<artifactId>relax-spring-boot-starter</artifactId>
<version>${latest-version}</version>
</dependency>
```
### 开启
项目启动类中使用 @EnableRelax

## 快速CRUD
Relax对API接口中的CRUD（创建、读取、更新、删除）场景进行了丰富的封装，大幅减少了开发人员在这方面的编码工作。
### 创建表
```java
@Data
@RelaxEntity(tableName = "relax_user")
public class RelaxUser {

    // 需使用 @RelaxId 显示指定主键,
    // 标注 @RelaxColumn 的属性会自动创建表字段,并且能够在查询中当作查询条件使用

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

}
```
### 创建接口
```java
//@RelaxClass中的路径前缀不能与其他控制器重复
@RelaxClass(prefix = "/relaxUser", entityType = RelaxUser.class)
@RestController
@RequestMapping("/relaxUser")
public class RelaxUserController {

}

```

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


