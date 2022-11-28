package com.petrovskiy.epm.mapper;

import com.petrovskiy.epm.dto.CategoryDto;
import com.petrovskiy.epm.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {
    Category dtoToCategory(CategoryDto categoryDto);
    CategoryDto categoryToDto(Category category);
}
