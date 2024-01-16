<!-- docs/_extend.md 扩展-->

## 代理
进行CRUD操作前或操作后的自定义业务逻辑，如特殊参数校验,业务校验,自定义返回格式等操作

### 自定义方法代理
```java
@Component
@RelaxProxy(relaxClass = RelaxUserController.class, proxyType = ProxyType.BEFORE)
public class RelaxUserAddAfterNode extends RelaxViewBeforeProxy {

    @Override
    public <T> T proxy(T t, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        // 方法执行前的自定义逻辑
        return t;
    }

    @Override
    public <T> boolean check(T t, ProxyMethodType proxyMethodType, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        // 什么情况下执行该节点的逻辑
        return false;
    }
}
```
自定义方法代理需要继承 **RelaxViewBeforeProxy** 或 **RelaxViewAfterProxy** 这两个类的其中一个，这两个类分别代表方法**前置代理**与方法**后置代理**，继承该类后需要实现两个方法 **proxy** 与 **check**
- **proxy**
  - **t** 用户自定义的实体类参数，即 **@RelaxEntity** 标识的类, RelaxViewBeforeProxy 类具有该参数
  - **o** 方法业务执行完返回的数据，如 info 接口为从数据库查到具体的BO对象数据, RelaxViewAfterProxy 类具有该参数
  - **httpServletRequest** 请求对象
  - **httpServletResponse** 响应对象
- **check**
  - **t** 用户自定义的实体类参数，即 **@RelaxEntity** 标识的类, RelaxViewBeforeProxy 类具有该参数
  - **o** 方法业务执行完返回的数据，如 info 接口为从数据库查到具体的BO对象数据, RelaxViewAfterProxy 类具有该参数
  - **proxyMethodType** 方法类型,如想让该节点仅执行 **add** 方法的前置逻辑，则可以使用 **ProxyMethodType.ADD == proxyMethodType** 来实现
  - **httpServletRequest** 请求对象
  - **httpServletResponse** 响应对象
  
使用 **@RelaxProxy** 注解对该代理节点进行注册  
ProxyMethodType参数如下
- **ADD** 新增
- **UPDATE** 更新
- **DELETE** 删除
- **LIST** 列表
- **PAGE** 分页
- **INFO** 详情
### 代理节点注册
代理注册分为注解与代码两种方式
#### 注解方式注册
```java
@Component
@RelaxProxy(relaxClass = RelaxUserController.class, proxyType = ProxyType.BEFORE)
public class RelaxUserAddAfterNode extends RelaxViewBeforeProxy {
    ...
}
```
注解方式在自定义的代理节点上使用 **@RelaxProxy** 注解标识该类，即可将该节点自动注册到执行器，该注解有三个参数，分别为
- **relaxClass** 必填 该参数标识该节点需要代理哪一个Controller类，一般为 **@RelaxClass** 标识的类
- **proxyType** 必填 该参数标识该节点是前置代理还是后置代理, 有 **ProxyType.BEFORE** (前置)、**ProxyType.AFTER**(后置)两种类型
- **afterClass** 非必填 该参数标识该节点需要在哪个节点后面执行
#### 代码方式注册
```java
@Component
public class RelaxProxyRegisterConfig implements RelaxProxyConfiguration {

    @Resource
    private RelaxUserAddAfterNode relaxUserAddAfterNode;

    @Override
    public void registerProxy() {
        RelaxProxyUtil.addProxy(RelaxUserController.class, relaxUserAddAfterNode, ProxyType.AFTER);
    }
}
```
代码方式注册需要实现 **RelaxProxyConfiguration**的**registerProxy**方法
使用 **RelaxProxyUtil**的**addProxy**方法，将自定义的代理节点添加到执行器
> 上面的类都要使用 **@Component** 注入到spring容器中  

**RelaxProxyUtil** 中有三个注册方法
- **addProxyBefore(relaxClass, beaforeClass, relaxProxy, proxyType)** 注册代理节点到某代理节点之前,参数依次为业务类、需到那个代理节点之前、需添加的代理节点、代理节点是前置或后置
- **addProxyAfter(relaxClass, afterClass, relaxProxy, proxyType)** 注册代理节点到某代理节点之后,参数依次为业务类、需到那个代理节点之后、需添加的代理节点、代理节点是前置或后置
- **addProxy(relaxClass, relaxProxy, proxyType)** 注册代理节点到执行器最后,参数依次为业务类、需添加的代理节点、代理节点是前置或后置
> 附: 系统默认代理节点，执行顺序从上至下  
> 前置代理节点  
> DefaultEntityFormatProxyNode 请求参数转实体对象类  
> DefaultValidateInfoProxyNode info接口校验类  
> DefaultInfoConversionProxyNode info接口对象类型校验类  
> DefaultValidatePageProxyNode page接口校验类  
> DefaultValidateListProxyNode list接口校验类  
> DefaultValidateDeleteProxyNode delete接口校验类  
> DefaultValidateUpdateProxyNode update接口校验类  
> DefaultValidateAddProxyNode add接口校验类  
> 后置代理节点  
> DefaultPageFormatProxyNode page返回对象构造类  
> DefaultListFormatProxyNode list返回对象构造类  
> DefaultInfoFormatProxyNode info返回对象构造类  
