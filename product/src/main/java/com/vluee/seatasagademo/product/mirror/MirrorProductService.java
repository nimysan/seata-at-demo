package com.vluee.seatasagademo.product.mirror;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "seatademo-product-mirror")
public interface MirrorProductService {


    @PostMapping("/mirror/products")
    public void createProduct(@RequestParam Integer productId, @RequestParam String productName);

    @PutMapping("/mirror/products/{productId}")
    public void updateProduct(@PathVariable Integer productId, @RequestParam String productName);
}
