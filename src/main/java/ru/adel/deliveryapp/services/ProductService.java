package ru.adel.deliveryapp.services;

import ru.adel.deliveryapp.models.OrderItem;
import ru.adel.deliveryapp.models.Product;

import java.util.List;
import java.util.Set;

public interface ProductService {
    List<Product> getAllProducts();
    Product getProductById(Long id);
    List<Product> getProductsByIds(Set<Long> productIds);
    Product createProduct(Product product);
    Product updateProduct(Long id, Product product);
    void deleteProduct(Long id);
    void updateProductStock(List<OrderItem> orderItems);
}
