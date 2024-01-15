<!-- docs/_extend.md 扩展-->

## 前置操作
进行CRUD操作前的特殊参数校验,业务校验等操作

### 自定义方法前置节点
```java
@Component
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
自定义前置节点需要继承 **RelaxViewBeforeProxy**，继承该类后需要实现两个方法 **proxy** 与 **check**
- proxy
  - t 用户自定义的实体类参数，即 **@RelaxEntity** 标识的类
  - httpServletRequest 请求对象
  - httpServletResponse 响应对象
- check
  - t 用户自定义的实体类参数，即 **@RelaxEntity** 标识的类
  - proxyMethodType 方法类型,如想让该节点仅执行 **add** 方法的前置逻辑，则可以使用 **ProxyMethodType.ADD == proxyMethodType**来实现
  - httpServletRequest 请求对象
  - httpServletResponse 响应对象

ProxyMethodType参数如下
- ADD 新增
- UPDATE 更新
- DELETE 删除
- LIST 列表
- PAGE 分页
- INFO 详情
### 前置节点注册
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
节点注册需要实现 **RelaxProxyConfiguration**的**registerProxy**方法
使用 **RelaxProxyUtil**的**addProxy**方法，将自定义的前置节点添加到容器
> 上面的类都要使用 **@Component** 注入到spring容器中


## 后置操作
进行CRUD操作后的日志记录,类型封装等操作