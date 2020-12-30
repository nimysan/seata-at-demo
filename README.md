# 事务处理例子

### 测试场景
product-service: productName包含111得时候，会导致异常
product-mirror-service: productName包含000得时候，会导致异常

服务发起方在： product-service/ProductService.java 里面会先调用mirror的更新操作， 然后更新Product的local database.

```
curl -X PUT "http://localhost:7075/products/1001?productName=producta_correct111"

curl -X POST "http://localhost:7075/products?productId=1001&productName=product-a"
```

###
