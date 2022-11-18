package ir.chaapaar.project.mapper;

import ir.chaapaar.project.dto.ProductDto;
import ir.chaapaar.project.entity.Product;

public class ProductMapper {
    public static Product mapProductDtoToEntity(ProductDto productDto) {
        return Product.builder()
                .name(productDto.getName())
                .price(productDto.getPrice())
                .build();
    }

    public static ProductDto mapProductEntityToDto(Product product) {
        return ProductDto.builder()
                .name(product.getName())
                .price(product.getPrice())
                .build();
    }
}
