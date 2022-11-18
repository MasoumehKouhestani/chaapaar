package ir.chaapaar.project.entity.product;

import ir.chaapaar.project.dto.ProductDto;
import ir.chaapaar.project.entity.Product;
import ir.chaapaar.project.exception.product.ProductNotFoundException;
import ir.chaapaar.project.mapper.ProductMapper;
import ir.chaapaar.project.repository.ProductRepository;
import ir.chaapaar.project.service.ProductService;
import ir.chaapaar.project.util.ProductTestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

import static ir.chaapaar.project.util.Constants.PRODUCT_NAME_1;
import static ir.chaapaar.project.util.Constants.PRODUCT_NAME_2;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
public class ProductServiceIntegrationTest {

    Random random = new Random();
    ProductTestUtil testUtil = new ProductTestUtil();
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;

    @Test
    public void testSaveAProductSuccessfully() {
        ProductDto product = testUtil.createProductDto(PRODUCT_NAME_1);
        Product savedProduct = productService.save(product);
        assertEquals(product.getName(), savedProduct.getName());
    }

    @Test
    public void testSaveAProductFails() {
        // ...
    }

    @Test
    public void testLoadByIdAProductThatExistsSuccessfully() {
        ProductDto product = testUtil.createProductDto(PRODUCT_NAME_1);
        Product savedProduct = productService.save(product);
        Integer productId = savedProduct.getId();
        Product productLoadedByService = productService.load(productId);
        assertEquals(product.getName(), productLoadedByService.getName());
    }

    @Test
    public void testLoadByIdAProductThatDoesNotExistFails() {
        assertThrows(ProductNotFoundException.class, () -> {
            productService.load(random.nextInt(100));
        });
    }

    @Test
    public void testLoadAllProducts_WhenTwoProductsAreSaved() {
        productService.save(testUtil.createProductDto(PRODUCT_NAME_1));
        productService.save(testUtil.createProductDto(PRODUCT_NAME_2));
        final List<Product> allProducts = productService.loadAll();
        assertEquals(2, allProducts.size());
    }

    @Test
    public void testUpdateAProductThatExistsSuccessfully() {
        ProductDto product = testUtil.createProductDto(PRODUCT_NAME_1);
        Product savedProduct = productService.save(product);
        savedProduct.setName(PRODUCT_NAME_2);
        Product updatedProduct = productService.update(savedProduct.getId(), ProductMapper.mapProductEntityToDto(savedProduct));
        assertEquals(savedProduct.getId(), updatedProduct.getId());
        assertEquals(PRODUCT_NAME_2, updatedProduct.getName());
    }

    @Test
    public void testUpdateAProductThatDoesNotExistFails() {
        assertThrows(ProductNotFoundException.class, () -> {
            productService.update(random.nextInt(100), testUtil.createProductDto(PRODUCT_NAME_1));
        });
    }

    @Test
    public void testDeleteAProductThatExistsSuccessfully() {
        ProductDto product = testUtil.createProductDto(PRODUCT_NAME_1);
        Product savedProduct = productService.save(product);
        productService.delete(savedProduct.getId());
        assertThrows(ProductNotFoundException.class, () -> {
            productService.load(savedProduct.getId());
        });
    }

    @Test
    public void testDeleteAProductThatDoesNotExistFails() {
        ProductDto product = testUtil.createProductDto(PRODUCT_NAME_1);
        Product savedProduct = productService.save(product);
        productService.delete(savedProduct.getId());
        assertThrows(ProductNotFoundException.class, () -> {
            productService.delete(savedProduct.getId());
        });
    }
}
