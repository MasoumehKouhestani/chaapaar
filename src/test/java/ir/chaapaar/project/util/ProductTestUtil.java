package ir.chaapaar.project.util;

import ir.chaapaar.project.dto.ProductDto;
import ir.chaapaar.project.entity.Product;

public class ProductTestUtil {

    public static final double PRODUCT_PRICE = 10.10;

    public ProductDto createProductDto(String name) {
        return ProductDto.builder()
                .name(name)
                .price(PRODUCT_PRICE)
                .build();
    }

    public Product createProduct(String name) {
        return Product.builder()
                .name(name)
                .price(PRODUCT_PRICE)
                .build();
    }
}
