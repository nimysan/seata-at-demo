# 使用 alibaba Seata (AT) 模式 做分布式事务的例子

> 本示例基于Spring boot 2.2.8.RELEASE和seata-server(1.4.0) 版本， 不同版本配置差异较大，请注意


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


### 测试场景
product-service: productName包含111得时候，会导致异常
product-mirror-service: productName包含000得时候，会导致异常

服务发起方在： product-service/ProductService.java 里面会先调用mirror的更新操作， 然后更新Product的local database.

```
curl -X PUT "http://localhost:7075/products/1001?productName=producta_correct111"

curl -X POST "http://localhost:7075/products?productId=1001&productName=product-a"
```

###
