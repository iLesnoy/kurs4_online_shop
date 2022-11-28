package com.petrovskiy.epm.mapper;

import com.petrovskiy.epm.dto.ResponseOrderDto;
import com.petrovskiy.epm.model.Order;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {UserMapper.class, ProductMapper.class}
        ,injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface OrderMapper{


    @Mapping(source = "userDto", target = "user")
    @Mapping(source = "productList", target = "productList")
    Order dtoToOrder(ResponseOrderDto order);

    @Mapping(source = "user", target = "userDto")
    @Mapping(source = "productList", target = "productList")
    ResponseOrderDto orderToDto(Order order);
}
