package com.vluee.seatasagademo.product.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mirror")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/products")
    public void createOrder(@RequestParam Integer productId, @RequestParam String productName) {
        productService.createProduct(productId, productName);
    }

    @PutMapping("/products/{productId}")
    public void updateProduct(@PathVariable Integer productId, @RequestParam String productName) {
        productService.updateProduct(productId, productName);
    }
}
