package com.petrovskiy.epm.impl;

import com.petrovskiy.epm.CategoryService;
import com.petrovskiy.epm.dao.ProductRepository;
import com.petrovskiy.epm.dao.CategoryRepository;
import com.petrovskiy.epm.dto.CategoryDto;
import com.petrovskiy.epm.exception.SystemException;
import com.petrovskiy.epm.mapper.CategoryMapper;
import com.petrovskiy.epm.model.Category;
import com.petrovskiy.epm.validator.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.petrovskiy.epm.exception.ExceptionCode.NON_EXISTENT_ENTITY;
import static com.petrovskiy.epm.exception.ExceptionCode.USED_ENTITY;


@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EntityValidator entityValidator;
    private final ProductRepository productRepository;
    private final CategoryMapper categoryMapper;


    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, EntityValidator entityValidator,
                               ProductRepository productRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.entityValidator = entityValidator;
        this.productRepository = productRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    @Transactional
    public CategoryDto create(CategoryDto categoryDto) {
        Category category = createCategory(categoryMapper.dtoToCategory(categoryDto));
        return categoryMapper.categoryToDto(category);
    }

    public Category createCategory(Category category) {
        return categoryRepository.findByName(category.getName()).orElseGet(() -> categoryRepository.save(category));
    }

    @Override
    public CategoryDto update(Long id, CategoryDto categoryDto) {
        Category updatedCategory = categoryRepository.findById(id).orElseGet(()-> categoryRepository.save(categoryMapper.dtoToCategory(categoryDto)));
        return categoryMapper.categoryToDto(updatedCategory);
    }

    @Override
    @Transactional
    public Page<CategoryDto> findAll(Pageable pageable) {
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        return new PageImpl<>(categoryPage.getContent(), categoryPage.getPageable(), categoryPage.getTotalElements())
                .map(categoryMapper::categoryToDto);
    }


    @Override
    public CategoryDto findById(Long id) {
        return categoryMapper.categoryToDto(findCategoryById(id));
    }

    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new SystemException(NON_EXISTENT_ENTITY));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            if (productRepository.findFirstByCategoryList_Id(id).isPresent()) {
                throw new SystemException(USED_ENTITY);
            }
            categoryRepository.delete(category.get());
        }else throw new SystemException(NON_EXISTENT_ENTITY);
    }

}
