package ir.chaapaar.project.entity.product;

import ir.chaapaar.project.entity.Product;
import ir.chaapaar.project.repository.ProductRepository;
import ir.chaapaar.project.util.ProductTestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static ir.chaapaar.project.util.Constants.PRODUCT_NAME_1;
import static ir.chaapaar.project.util.Constants.PRODUCT_NAME_2;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
public class ProductRepositoryUnitTest {

    Random random = new Random();
    ProductTestUtil testUtil = new ProductTestUtil();
    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testSaveAndLoadAProductSuccessfully() {
        Product product = testUtil.createProduct(PRODUCT_NAME_1);
        Product savedProduct = productRepository.save(product);
        assertEquals(product.getName(), savedProduct.getName());
    }

    @Test
    public void testSaveAProductFails() {
        // ...
    }

    @Test
    public void testLoadAProductThatDoesNotExistFails() {
        Optional<Product> product = productRepository.findById(random.nextInt(100));
        assertFalse(product.isPresent());
    }

    @Test
    public void testLoadAllProducts_WhenTwoProductsAreSaved() {
        productRepository.save(testUtil.createProduct(PRODUCT_NAME_1));
        productRepository.save(testUtil.createProduct(PRODUCT_NAME_2));
        final List<Product> allProducts = productRepository.findAll();
        assertEquals(2, allProducts.size());
    }

    @Test
    public void testUpdateAProductThatExistsSuccessfully() {
        Product product = testUtil.createProduct(PRODUCT_NAME_1);
        Product savedProduct = productRepository.save(product);
        savedProduct.setName(PRODUCT_NAME_2);
        Product updatedProduct = productRepository.save(savedProduct);
        assertEquals(savedProduct.getId(), updatedProduct.getId());
    }

    @Test
    public void testUpdateAProductThatDoesNotExistFails() {
        // ...
    }

    @Test
    public void testDeleteAProductThatExistsSuccessfully() {
        Product product = testUtil.createProduct(PRODUCT_NAME_1);
        Product savedProduct = productRepository.save(product);
        productRepository.delete(savedProduct);
        Optional<Product> deletedProduct = productRepository.findById(savedProduct.getId());
        assertFalse(deletedProduct.isPresent());
    }

    @Test
    public void testDeleteAProductThatDoesNotExistFails() {
        // ...
    }
}
