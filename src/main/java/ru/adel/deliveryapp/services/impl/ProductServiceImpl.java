package ru.adel.deliveryapp.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.adel.deliveryapp.models.OrderItem;
import ru.adel.deliveryapp.models.Product;
import ru.adel.deliveryapp.repositories.ProductRepository;
import ru.adel.deliveryapp.services.ProductService;
import ru.adel.deliveryapp.util.InsufficientStockException;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;


    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
    }

    public List<Product> getProductsByIds(Set<Long> productIds) {
        return productRepository.findAllById(productIds);
    }

    @Transactional
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long id, Product product) {
        Product existingProduct = getProductById(id);
        enrichProduct(existingProduct, product);
        return productRepository.save(existingProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Transactional
    public void updateProductStock(List<OrderItem> orderItems) {
        List<Product> updatedProducts = new ArrayList<>();

        for (OrderItem orderItem : orderItems) {
            Product product = orderItem.getProduct();
            Long currentStock = product.getStock();
            Long orderedQuantity = orderItem.getQuantity();
            Long updatedStock = currentStock - orderedQuantity;

            // Проверка наличия достаточного количества товара на складе
            if (updatedStock < 0) {
                throw new InsufficientStockException("Insufficient stock for product: " + product.getName());
            }

            product.setStock(updatedStock);
            updatedProducts.add(product);
        }

        // Пакетное обновление количества товаров в базе данных
        productRepository.saveAll(updatedProducts);
    }

    private void enrichProduct(Product existingProduct, Product newProduct) {
        if (newProduct.getName() != null) {
            existingProduct.setName(newProduct.getName());
        }
        if (newProduct.getPrice() != null) {
            existingProduct.setPrice(newProduct.getPrice());
        }
    }
}
