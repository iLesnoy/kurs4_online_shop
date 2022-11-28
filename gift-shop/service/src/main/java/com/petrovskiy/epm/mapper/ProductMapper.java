package com.petrovskiy.epm.mapper;

import com.petrovskiy.epm.dto.ProductDto;
import com.petrovskiy.epm.model.Product;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = CategoryMapper.class,injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ProductMapper {
    @Mapping(target = "categoryList",source = "categoryDtoList")
    Product dtoToProduct(ProductDto productDto);
    @Mapping(target = "categoryDtoList",source = "categoryList")
    ProductDto productToDto(Product product);
}
