package com.petrovskiy.epm.impl;

import com.petrovskiy.epm.dto.RequestOrderDto;
import com.petrovskiy.epm.dto.ResponseOrderDto;
import com.petrovskiy.epm.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Override
    public ResponseOrderDto create(ResponseOrderDto responseOrderDto) {
        return null;
    }

    @Override
    public ResponseOrderDto update(Long id, ResponseOrderDto responseOrderDto) {
        return null;
    }

    @Override
    public ResponseOrderDto findById(Long id) {
        return null;
    }

    @Override
    public Page<ResponseOrderDto> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public ResponseOrderDto create(RequestOrderDto order) {
        return null;
    }
}