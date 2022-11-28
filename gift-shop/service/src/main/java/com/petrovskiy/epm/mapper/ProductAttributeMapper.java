package com.petrovskiy.epm.mapper;

import com.petrovskiy.epm.dto.ProductAttributeDto;
import com.petrovskiy.epm.model.ProductAttribute;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductAttributeMapper {
    ProductAttribute convert(ProductAttributeDto source);
}
