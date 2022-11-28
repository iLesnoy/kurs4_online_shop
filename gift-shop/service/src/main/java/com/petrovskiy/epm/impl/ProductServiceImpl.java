package com.petrovskiy.epm.impl;

import com.petrovskiy.epm.ProductService;
import com.petrovskiy.epm.dao.ProductRepository;
import com.petrovskiy.epm.dao.OrderRepository;
import com.petrovskiy.epm.dto.CategoryDto;
import com.petrovskiy.epm.dto.CustomPage;
import com.petrovskiy.epm.dto.ProductAttributeDto;
import com.petrovskiy.epm.dto.ProductDto;
import com.petrovskiy.epm.exception.SystemException;
import com.petrovskiy.epm.mapper.CategoryMapper;
import com.petrovskiy.epm.mapper.ProductMapper;
import com.petrovskiy.epm.model.Product;
import com.petrovskiy.epm.model.Category;
import com.petrovskiy.epm.validator.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.petrovskiy.epm.exception.ExceptionCode.*;
import static org.springframework.data.domain.Sort.by;

@Service
public class ProductServiceImpl implements ProductService {


    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final CategoryServiceImpl categoryService;
    private final CategoryMapper categoryMapper;
    private final EntityValidator validator;
    private final OrderRepository orderRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CategoryServiceImpl categoryService,
                              OrderRepository orderRepository, ProductMapper productMapper,
                              CategoryMapper categoryMapper, EntityValidator validator) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.orderRepository = orderRepository;
        this.productMapper = productMapper;
        this.categoryMapper = categoryMapper;
        this.validator = validator;
    }

    @Override
    @Transactional
    public ProductDto create(ProductDto productDto) {
        productRepository.findByName(productDto.getName()).ifPresent(product -> {
            throw new SystemException(DUPLICATE_NAME);
        });
        Product product = productMapper.dtoToProduct(productDto);
        setCategoryList(product);
        return productMapper.productToDto(productRepository.save(product));
    }


    @Override
    @Transactional
    public ProductDto update(Long id, ProductDto productDto) {
        AtomicReference<Product> atomicProduct = new AtomicReference<>();
        productRepository.findById(id).ifPresentOrElse(product -> {
            setUpdatedFields(product, productDto);
            setUpdatedCategoryList(product, productDto.getCategoryDtoList());
            atomicProduct.set(productRepository.save(product));

        }, () -> {
            throw new SystemException(NON_EXISTENT_ENTITY);
        });
        return productMapper.productToDto(atomicProduct.get());
    }

    @Override
    public ProductDto findById(Long id) {
        return productRepository.findById(id).map(productMapper::productToDto)
                .orElseThrow(() -> new SystemException((NON_EXISTENT_ENTITY)));
    }

    @Override
    public Product findProductById(Long id) {
        return productMapper.dtoToProduct(findById(id));
    }

    @Override
    public Page<ProductDto> findAll(Pageable pageable) {
        throw new UnsupportedOperationException("command is not supported, please use searchByParameters");
    }

    @Override
    @Transactional
    public Page<ProductDto> searchByParameters(ProductAttributeDto attributeDto, Pageable pageable) {
        setDefaultParamsIfAbsent(attributeDto);
        Pageable sortedPageable = buildPageableSort(attributeDto.getSortingFieldList(), attributeDto.getOrderSort(), pageable);
        Page<Product> productPage = Objects.nonNull(attributeDto.getCategoryNameList())
                ? productRepository.findByAttributes(attributeDto.getCategoryNameList()
                , attributeDto.getCategoryNameList().size(), attributeDto.getSearchPart(), sortedPageable)
                : productRepository.findByAttributes(attributeDto.getSearchPart(), sortedPageable);
        if (!validator.isPageExists(pageable, productPage.getTotalElements())) {
            throw new SystemException(NON_EXISTENT_PAGE);
        }
        return new CustomPage<>(productPage.getContent(), productPage.getPageable(), productPage.getTotalElements())
                .map(productMapper::productToDto);
    }

    private void setDefaultParamsIfAbsent(ProductAttributeDto attributeDto) {
        if (Objects.isNull(attributeDto.getSearchPart())) {
            attributeDto.setSearchPart("");
        }
    }

    private Pageable buildPageableSort(List<String> sortingFieldList, String orderSort, Pageable pageable) {
        Sort.Direction direction = Objects.nonNull(orderSort)
                ? Sort.Direction.fromString(orderSort)
                : Sort.Direction.ASC;
        return CollectionUtils.isEmpty(sortingFieldList)
                ? PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(direction, "id"))
                : PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()
                , by(direction, sortingFieldList.toArray(String[]::new)));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        optionalProduct.ifPresentOrElse(a -> {
            orderRepository.findFirstByProductListId(id).ifPresentOrElse(b -> {
                throw new SystemException(USED_ENTITY);
            }, () -> productRepository.delete(optionalProduct.get()));
        }, () -> {
            throw new SystemException(NON_EXISTENT_ENTITY);
        });
    }


    private void setUpdatedCategoryList(Product persistedProduct, List<CategoryDto> categoryDtoList) {
        Set<Category> updatedCategorySet = categoryDtoList.stream()
                .map(categoryMapper::dtoToCategory)
                .map(categoryService::createCategory)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        persistedProduct.setCategoryList(updatedCategorySet);
    }

    private void setUpdatedFields(Product persistedProduct, ProductDto updatedProduct) {
        String name = updatedProduct.getName();
        String description = updatedProduct.getDescription();
        BigDecimal price = updatedProduct.getPrice();
        int duration = updatedProduct.getDuration();

        persistedProduct.setName(name);
        persistedProduct.setDescription(description);
        persistedProduct.setPrice(price);
        persistedProduct.setDuration(duration);
    }

    private void setCategoryList(Product product) {
        product.setCategoryList(product.getCategoryList().stream().map(categoryService::createCategory)
                .collect(Collectors.toSet()));
    }

}