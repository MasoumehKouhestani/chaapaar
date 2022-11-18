package ir.chaapaar.project.entity.product;

import ir.chaapaar.project.controller.ProductController;
import ir.chaapaar.project.dto.ProductDto;
import ir.chaapaar.project.entity.Product;
import ir.chaapaar.project.mapper.ProductMapper;
import ir.chaapaar.project.repository.ProductRepository;
import ir.chaapaar.project.util.ProductTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

import static ir.chaapaar.project.util.Constants.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerIntegrationTest {

    ProductTestUtil testUtil = new ProductTestUtil();
    @LocalServerPort
    private int port;
    private String baseUrl = "";
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductController productController;
    @Autowired
    private TestRestTemplate restTemplate;
    Random random = new Random();

    @BeforeEach
    public void setup() {
        baseUrl = "http://localhost:" + port + "/chaapaar/product";
    }

    @Test
    public void testSaveAProductSuccessfully() {
        ProductDto product = testUtil.createProductDto(PRODUCT_NAME_1);
        ResponseEntity<Product> saveRequestResponse = restTemplate.postForEntity(baseUrl + "/save", product, Product.class);
        assertEquals(HttpStatus.OK, saveRequestResponse.getStatusCode());

        Product loadedProduct = productRepository.findByName(PRODUCT_NAME_1);
        assertEquals(product.getName(), loadedProduct.getName());
    }

    @Test
    public void testSaveAProductFails() {
        // ...
    }

    @Test
    public void testLoadByIdAProductThatExistsSuccessfully() {
        ProductDto product = testUtil.createProductDto(PRODUCT_NAME_2);
        ResponseEntity<Product> saveRequestResponse = restTemplate.postForEntity(baseUrl + "/save", product, Product.class);
        Integer productId = saveRequestResponse.getBody().getId();
        ResponseEntity<Product> loadRequestResponse = restTemplate.getForEntity(baseUrl + "/load/" + productId, Product.class);
        assertEquals(HttpStatus.OK, loadRequestResponse.getStatusCode());
        assertEquals(productId, loadRequestResponse.getBody().getId());
        assertEquals(PRODUCT_NAME_2, loadRequestResponse.getBody().getName());
    }

    @Test
    public void testLoadByIdAProductThatDoesNotExistFails() {
        ResponseEntity<Product> loadRequestResponse = restTemplate.getForEntity(baseUrl + "/load/" + random.nextInt(100), Product.class);
        assertNull(loadRequestResponse.getBody());
    }

    @Test
    public void testLoadAllProducts_WhenTwoProductsAreSaved() {
        restTemplate.execute(baseUrl + "/delete-all", HttpMethod.GET, null, null);
        ProductDto product1 = testUtil.createProductDto(PRODUCT_NAME_3);
        restTemplate.postForEntity(baseUrl + "/save", product1, Product.class);
        ProductDto product2 = testUtil.createProductDto(PRODUCT_NAME_4);
        restTemplate.postForEntity(baseUrl + "/save", product2, Product.class);
        ResponseEntity<Product[]> response = restTemplate.getForEntity(baseUrl + "/load-all/", Product[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().length);
    }

    @Test
    public void testUpdateAProductThatExistsSuccessfully() {
        ProductDto product = testUtil.createProductDto(PRODUCT_NAME_5);
        ResponseEntity<Product> saveRequestResponse = restTemplate.postForEntity(baseUrl + "/save", product, Product.class);
        Product savedProduct = saveRequestResponse.getBody();
        savedProduct.setName(PRODUCT_NAME_6);
        ResponseEntity<Product> updatedRequestResponse = restTemplate.postForEntity(baseUrl + "/update/" + savedProduct.getId(),
                ProductMapper.mapProductEntityToDto(savedProduct), Product.class);
        assertEquals(HttpStatus.OK, updatedRequestResponse.getStatusCode());
        assertEquals(savedProduct.getId(), updatedRequestResponse.getBody().getId());
        assertEquals(PRODUCT_NAME_6, updatedRequestResponse.getBody().getName());
    }

    @Test
    public void testUpdateAProductThatDoesNotExistFails() {
        ResponseEntity<Product> updatedRequestResponse = restTemplate.postForEntity(baseUrl + "/update/" + random.nextInt(100),
                testUtil.createProductDto(PRODUCT_NAME_1), Product.class);
        assertEquals(HttpStatus.NOT_FOUND, updatedRequestResponse.getStatusCode());
        assertNull(updatedRequestResponse.getBody());
    }

    @Test
    public void testDeleteAProductThatExistsSuccessfully() {
        ProductDto product = testUtil.createProductDto(PRODUCT_NAME_7);
        ResponseEntity<Product> saveRequestResponse = restTemplate.postForEntity(baseUrl + "/save", product, Product.class);
        Product savedProduct = saveRequestResponse.getBody();
        ResponseEntity<Product> deleteRequestResponse = restTemplate.getForEntity(baseUrl + "/delete/" + savedProduct.getId(), Product.class);
        assertEquals(HttpStatus.OK, deleteRequestResponse.getStatusCode());

        assertFalse(productRepository.findById(savedProduct.getId()).isPresent());
    }

    @Test
    public void testDeleteAProductThatDoesNotExistFails() {
        ResponseEntity<Product> deleteRequestResponse = restTemplate.getForEntity(baseUrl + "/delete/" + random.nextInt(100), Product.class);
        assertEquals(HttpStatus.NOT_FOUND, deleteRequestResponse.getStatusCode());
        assertNull(deleteRequestResponse.getBody());
    }
}
