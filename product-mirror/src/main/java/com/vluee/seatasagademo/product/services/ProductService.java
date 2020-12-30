package com.vluee.seatasagademo.product.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;


    public Product createProduct(Integer productId, String name) {

        checkName(name);
        //真实创建
        Product product = new Product();
        product.setId(productId);
        product.setName(name);
        productRepository.save(product);

        return product;
    }

    private void checkName(String name) {
        if (name.contains("000")) {
            throw new RuntimeException("Product name can't contain 000");
        }
    }

    public void updateProduct(Integer productId, String productName) {
        checkName(productName);
        Product product = productRepository.findById(productId).orElseThrow(RuntimeException::new);
        product.setName(productName);
        productRepository.save(product);
    }
}
