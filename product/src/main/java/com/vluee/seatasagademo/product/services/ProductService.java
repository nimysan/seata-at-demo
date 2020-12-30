package com.vluee.seatasagademo.product.services;

import com.vluee.seatasagademo.product.mirror.MirrorProductService;
import io.seata.spring.annotation.GlobalTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private Logger log = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MirrorProductService mirrorProductService;

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

    private void mockForException(String name) {
        boolean invalid = name.contains("111");
        if (invalid) throw new IllegalArgumentException("Product name can't not contain 111 ");
    }

    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public void updateProduct(Integer productId, String productName) {

        //调用外部服务创建
        mirrorProductService.updateProduct(productId, productName);


        Product product = productRepository.findById(productId).orElseThrow(RuntimeException::new);
        product.setName(productName);
        productRepository.save(product);

        mockForException(productName);

    }
}
