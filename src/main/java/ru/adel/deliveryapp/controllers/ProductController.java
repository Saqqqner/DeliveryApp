package ru.adel.deliveryapp.controllers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.adel.deliveryapp.dto.ProductDTO;
import ru.adel.deliveryapp.models.Product;
import ru.adel.deliveryapp.services.impl.ProductServiceImpl;
import ru.adel.deliveryapp.util.valid.ProductDTOValidator;

import java.util.List;
import java.util.stream.Collectors;

import static ru.adel.deliveryapp.util.ErrorsUtil.returnErrorsToClient;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductServiceImpl productServiceImpl;
    private final ModelMapper modelMapper;
    private final ProductDTOValidator productDTOValidator;


    @GetMapping()
    public ResponseEntity<List<ProductDTO>> getProducts() {
        List<Product> products = productServiceImpl.getAllProducts();
        if (products.isEmpty()) return ResponseEntity.notFound().build();
        List<ProductDTO> productDTOS = products.stream().map(product -> modelMapper.map(product, ProductDTO.class)).collect(Collectors.toList());
        return ResponseEntity.ok(productDTOS);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long productId) {
        Product product = productServiceImpl.getProductById(productId);
        if (product == null) return ResponseEntity.notFound().build();
        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
        return ResponseEntity.ok(productDTO);
    }

    @PostMapping("/create")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody @Validated ProductDTO productDTO, BindingResult bindingResult) {
        productDTOValidator.validate(productDTO, bindingResult);
        if (bindingResult.hasErrors()) returnErrorsToClient(bindingResult);

        Product product = modelMapper.map(productDTO, Product.class);
        Product createdProduct = productServiceImpl.createProduct(product);
        ProductDTO createdProductDTO = modelMapper.map(createdProduct, ProductDTO.class);
        return new ResponseEntity<>(createdProductDTO, HttpStatus.CREATED);
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long productId, @RequestBody @Validated ProductDTO productDTO, BindingResult bindingResult) {
        productDTOValidator.validate(productDTO, bindingResult);
        if (bindingResult.hasErrors()) returnErrorsToClient(bindingResult);
        Product product = modelMapper.map(productDTO, Product.class);
        Product updatedProduct = productServiceImpl.updateProduct(productId, product);
        ProductDTO updatedProductDTO = modelMapper.map(updatedProduct, ProductDTO.class);
        return ResponseEntity.ok(updatedProductDTO);
    }

    @DeleteMapping("/delete/{productId}")
    public void deleteProduct(@PathVariable Long productId) {
        productServiceImpl.deleteProduct(productId);
    }
}
