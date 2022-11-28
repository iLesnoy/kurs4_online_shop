package com.petrovskiy.epm;

import com.petrovskiy.epm.dto.CategoryDto;

public interface CategoryService extends BaseService<CategoryDto> {
    CategoryDto create(CategoryDto categoryDto);

    CategoryDto update(Long id, CategoryDto categoryDto);

    CategoryDto findById(Long id);

    void delete(Long id);
}
