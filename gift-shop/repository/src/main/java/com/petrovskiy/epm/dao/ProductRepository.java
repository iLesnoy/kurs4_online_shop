package com.petrovskiy.epm.dao;

import com.petrovskiy.epm.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.petrovskiy.epm.constants.SqlQuery.*;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    @Query(FIND_PRODUCTS_BY_SEARCH_PART)
    Page<Product> findByAttributes(@Param("searchPart") String searchPart, Pageable pageable);

    @Query(FIND_PRODUCTS_BY_CATEGORY_NAMES_AND_SEARCH_PART_ANTON)
    Page<Product> findByAttributes(@Param("categoryNameList") List<String> categoryNameList, @Param("searchPart") String searchPart, Pageable pageable);

    Optional<Product> findFirstByCategoryList_Id(Long Id);

    Optional<Product> findByName(String name);
}
