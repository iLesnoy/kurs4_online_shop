package com.petrovskiy.epm.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductAttribute {

    private List<String> categoryNameList;
    private String searchPart;
    private String orderSort;
    private List<String> sortingFieldList;


}