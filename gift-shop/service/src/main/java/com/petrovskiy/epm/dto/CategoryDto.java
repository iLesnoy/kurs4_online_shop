package com.petrovskiy.epm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;


@Data
@JsonPropertyOrder({"id", "name"})
public class CategoryDto extends RepresentationModel<CategoryDto> {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String name;
}