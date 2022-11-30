package com.petrovskiy.epm.controller;

import com.petrovskiy.epm.ProductService;
import com.petrovskiy.epm.dto.AuthenticationRequestDto;
import com.petrovskiy.epm.dto.ProductAttributeDto;
import com.petrovskiy.epm.dto.ProductDto;
import com.petrovskiy.epm.model.Order;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/products")
@Slf4j
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

    @GetMapping("about")
    public String showAboutPage() {
        return "about";
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    /*@PreAuthorize("hasAuthority('update')")*/
    public ProductDto update(@PathVariable Long id,
                             @RequestBody ProductDto productDto) {
        return productService.update(id, productDto);
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model) {
        ProductDto productDto = productService.findById(id);
        model.addAttribute("product", productDto);
        return "products";
    }

    @GetMapping
    public String findByAttributes(ProductAttributeDto attribute, @PageableDefault(size = 5, page = 0) Pageable pageable,
                                   Model model) {
        Page<ProductDto> page = productService.searchByParameters(attribute, pageable);

        int totalPages = page.getTotalPages() - 1;
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(0, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
            model.addAttribute("productList", page.getContent());
            model.addAttribute("page", page);
        return "products";
    }


        @DeleteMapping("/{id}")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        /*@PreAuthorize("hasAuthority('delete')")*/
        public void deleteById (@PathVariable Long id){
            productService.delete(id);
        }

}