package ir.chaapaar.project.service;

import ir.chaapaar.project.dto.ProductDto;
import ir.chaapaar.project.entity.Product;
import ir.chaapaar.project.exception.product.ProductNotFoundException;
import ir.chaapaar.project.mapper.ProductMapper;
import ir.chaapaar.project.repository.ProductRepository;
import ir.chaapaar.project.util.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Transactional
    public Product save(ProductDto productDto) {
        Product product = productRepository.save(ProductMapper.mapProductDtoToEntity(productDto));
        log.warn(LogUtils.encode(String.format("Product with name «%s» saved.", product.getName())));
        return product;
    }

    @Transactional
    public Product load(int id) {
        Optional<Product> product = productRepository.findById(id);
        if (!product.isPresent()) {
            throw new ProductNotFoundException(String.format("Product with id %s not found!", id));
        }
        log.warn(LogUtils.encode(String.format("Product with id «%s» loaded.", id)));
        return product.get();
    }

    @Transactional
    public List<Product> loadAll() {
        List<Product> products = productRepository.findAll();
        log.warn(LogUtils.encode("All products loaded."));
        return products;
    }

    @Transactional
    public Product update(int id, ProductDto productDto) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            Product newProduct = ProductMapper.mapProductDtoToEntity(productDto);
            newProduct.setId(product.get().getId());
            Product updatedProduct = productRepository.save(newProduct);
            log.warn(LogUtils.encode(String.format("Product with id «%s» updates.", updatedProduct.getId())));
            return updatedProduct;
        } else {
            throw new ProductNotFoundException(String.format("Product with id %s not found!", id));
        }
    }

    @Transactional
    public Product delete(int id) {
        final Optional<Product> product = productRepository.findById(id);
        if (!product.isPresent()) {
            throw new ProductNotFoundException(String.format("Product with id %s not found!", id));
        }
        productRepository.delete(product.get());
        log.warn(LogUtils.encode(String.format("Product with id «%s» deleted.", id)));
        return product.get();
    }

    @Transactional
    public void deleteAll() {
        productRepository.deleteAll();
    }
}
