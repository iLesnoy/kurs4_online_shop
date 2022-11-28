package com.petrovskiy.epm;

import com.petrovskiy.epm.dto.ProductAttributeDto;
import com.petrovskiy.epm.dto.ProductDto;
import com.petrovskiy.epm.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService extends BaseService<ProductDto> {
    Page<ProductDto> searchByParameters(ProductAttributeDto attributeDto, Pageable pageable);
    Product findProductById(Long id);
}
