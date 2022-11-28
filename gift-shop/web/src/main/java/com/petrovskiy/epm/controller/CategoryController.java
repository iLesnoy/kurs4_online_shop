package com.petrovskiy.epm.controller;

import com.petrovskiy.epm.dto.CategoryDto;
import com.petrovskiy.epm.dto.ProductAttributeDto;
import com.petrovskiy.epm.dto.ProductDto;
import com.petrovskiy.epm.impl.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryServiceImpl categoryService;

    @Autowired
    public CategoryController(CategoryServiceImpl categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    /*@PreAuthorize("hasAuthority('create')")*/
    public CategoryDto create(@Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto created = categoryService.create(categoryDto);
        return created;
    }

    @GetMapping("/{id}")
    public CategoryDto findById(@PathVariable Long id) {
        CategoryDto categoryDto = categoryService.findById(id);
        return categoryDto;
    }

    @GetMapping
    public String findAll(@PageableDefault(size = 5, page = 0) Pageable pageable,
                                     Model model) {
        Page<CategoryDto> page = categoryService.findAll(pageable);
        model.addAttribute("pageSize",page.getNumberOfElements());
        model.addAttribute("pageNumbers", page.getNumber());
        model.addAttribute("page",page);
        model.addAttribute("productList",page.getContent());
        return "allProductsPage";
    }

    @PatchMapping("/update")
    public CategoryDto updateCategory(@Valid @RequestBody CategoryDto categoryDto){
        return categoryService.update(categoryDto.getId(), categoryDto);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    /*@PreAuthorize("hasAuthority('delete')")*/
    public void delete(@PathVariable Long id) {
        categoryService.delete(id);
    }
}