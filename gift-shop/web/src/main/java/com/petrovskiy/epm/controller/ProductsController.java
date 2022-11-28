package com.petrovskiy.epm.controller;

import com.petrovskiy.epm.ProductService;
import com.petrovskiy.epm.dto.ProductAttributeDto;
import com.petrovskiy.epm.dto.ProductDto;
import com.petrovskiy.epm.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/api/products")
public class ProductsController {

    private final ProductService productService;

    @Autowired
    public ProductsController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    /*@PreAuthorize("hasAuthority('create')")*/
    public ProductDto create(@Valid @RequestBody ProductDto productDto) {
        ProductDto created = productService.create(productDto);
        return created;
    }


    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    /*@PreAuthorize("hasAuthority('update')")*/
    public ProductDto update(@PathVariable Long id,
                             @RequestBody ProductDto productDto) {
        return productService.update(id, productDto);
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id,Model model) {
        ProductDto productDto = productService.findById(id);
        model.addAttribute("product",productDto);
        return "allProductsPage";
    }

    @GetMapping
    public String findByAttributes(ProductAttributeDto attribute,@PageableDefault(size = 5, page = 0) Pageable pageable,
                                   Model model) {
        Page<ProductDto> page = productService.searchByParameters(attribute, pageable);
        model.addAttribute("pageSize",page.getNumberOfElements());
        model.addAttribute("pageNumbers", page.getNumber());
        model.addAttribute("page",page);
        model.addAttribute("productList",page.getContent());
        return "allProductsPage";
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    /*@PreAuthorize("hasAuthority('delete')")*/
    public void deleteById(@PathVariable Long id) {
        productService.delete(id);
    }
}