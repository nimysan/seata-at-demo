# 使用 alibaba Seata (AT) 模式 做分布式事务的例子

> 本示例基于Spring boot 2.2.8.RELEASE和seata-server(1.4.0) 版本， 不同版本配置差异较大，请注意


## Seata模式说明
1. AT 自动模式， 通过GlobalTransaction注解， 自动将关联事务注册到一个xid
2. XA 基于XA模式的的事务， 与AT的编程模型完全相同，只是需要数据源切换为 XA DataSource proxy [XA Mode](https://seata.io/zh-cn/docs/dev/mode/xa-mode.html)
3. TCC TRY-CONFIRM-CANCEL 自定义行为纳入事务框架
4. SAGA SAGA标准

## 测试场景说明

product服务为主服务， 该服务会写入数据进product表。 Product-mirror为product镜像服务，每次product的修改都期望同时修改这两个库。

## 准备

1. mariadb (localhost:3306) (三个库： seata-server/product/product-mirror) - 初始化脚本见: sqls目录。

2. nacos (用作服务注册之用) 结合spring-cloud作为微服务注册管理平台
``` bash
#进入deploy/nacos目录， 执行
docker stack deploy -c nacos-service.yaml demonacos 
#删除
docker stack rm demonacos
```
启动后 访问： http://localhost:8848/nacos 
用户名和密码： nacos/nacos

3. seata-server (版本1.4.1)
进入 deploy/seata  进入bin
```groovy
seata-server.bat
```   

## 服务说明

1. product: 新增/修改 数据库product表
>> 当productName参数中含有111字符串的时候，会导致runtime exception (辅助测试)

2. product-mirror: 新增/修改 数据库 product表

>>当productName含000的时候，抛出runtime exception(辅助测试)

```java
  /**
     *  @GlobalTransactional确保全局事务开始
     *
     *  @Transactional 确保本地事务支持
     *
     * @param productId
     * @param name
     * @return
     */
    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public Product createProduct(Integer productId, String name) {


        log.info("step1： create mirror product");
        //调用外部服务创建
        mirrorProductService.createProduct(productId, name);

        log.info("step2： create local product");
        //真实创建
        Product product = new Product();
        product.setId(productId);
        product.setName(name);

        productRepository.save(product);

        mockForException(name);

        return product;
    }
```

### 期望结果

product/product-mirror各自的数据库中，相同productId的productName保持一致。


#### 测试工具(最好用postman)

```
PUT "http://localhost:7075/products/1001?productName=producta_correct111"

POST "http://localhost:7075/products?productId=1001&productName=product-a"
```

