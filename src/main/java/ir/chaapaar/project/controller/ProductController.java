package ir.chaapaar.project.controller;

import ir.chaapaar.project.dto.ProductDto;
import ir.chaapaar.project.entity.Product;
import ir.chaapaar.project.exception.product.ProductNotFoundException;
import ir.chaapaar.project.service.ProductService;
import ir.chaapaar.project.util.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("product")
@Slf4j
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Product save(@RequestBody ProductDto product) {
        return productService.save(product);
    }

    //TODO: is that ok returning null (load and update and delete)
    @GetMapping(value = "/load/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Product load(@PathVariable(value = "id") Integer id) {
        try {
            return productService.load(id);
        } catch (ProductNotFoundException e) {
            log.warn(LogUtils.encode(String.format("Product with id %s not found!", id)));
            return null;
        }
    }

    @GetMapping(value = "/load-all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Product> loadAll() {
        return productService.loadAll();
    }

    @PostMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Product update(@PathVariable(value = "id") Integer id, @RequestBody ProductDto product) {
        try {
            return productService.update(id, product);
        } catch (ProductNotFoundException e) {
            log.warn(LogUtils.encode(String.format("Product with id %s not found!", id)));
            return null;
        }
    }

    @GetMapping("/delete/{id}")
    public Product delete(@PathVariable(value = "id") Integer id) {
        try {
            return productService.delete(id);
        } catch (ProductNotFoundException e) {
            log.warn(LogUtils.encode(String.format("Product with id %s not found!", id)));
            return null;
        }
    }

    @GetMapping("/delete-all")
    public void delete() {
        productService.deleteAll();
    }
}
