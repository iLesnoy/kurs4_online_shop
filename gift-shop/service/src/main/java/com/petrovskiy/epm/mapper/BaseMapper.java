package com.petrovskiy.epm.mapper;

public interface BaseMapper <T,B>{

    B entityToDto(T entity);
    T dtoToEntity(B dto);
}
