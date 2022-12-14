package com.petrovskiy.epm.impl;

import com.petrovskiy.epm.OrderService;
import com.petrovskiy.epm.dao.OrderRepository;
import com.petrovskiy.epm.dto.CustomPage;
import com.petrovskiy.epm.dto.RequestOrderDto;
import com.petrovskiy.epm.dto.ResponseOrderDto;
import com.petrovskiy.epm.exception.SystemException;
import com.petrovskiy.epm.mapper.OrderMapper;
import com.petrovskiy.epm.model.Product;
import com.petrovskiy.epm.model.Order;
import com.petrovskiy.epm.model.User;
import com.petrovskiy.epm.validator.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.petrovskiy.epm.exception.ExceptionCode.*;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final EntityValidator validator;
    private final UserServiceImpl userService;
    private final ProductServiceImpl productService;
    private final OrderMapper orderMapper;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, EntityValidator validator, UserServiceImpl userService,
                            ProductServiceImpl productService,
                            OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.validator = validator;
        this.userService = userService;
        this.productService = productService;
        this.orderMapper = orderMapper;
    }

    @Override
    public ResponseOrderDto create(ResponseOrderDto responseOrderDto) {
        throw new UnsupportedOperationException("command is not supported in OrderServiceImpl class ");
    }

    @Override
    @Transactional
    public ResponseOrderDto create(RequestOrderDto orderDto) {
        User user = userService.findUserById(orderDto.getUserId());
        List<Product> products = orderDto.getProductIdList()
                .stream().map(productService::findProductById).collect(Collectors.toList());
        Order order = Order.builder()
                .user(user)
                .productList(products).build();
        return orderMapper.orderToDto(orderRepository.save(order));
    }

    @Override
    public ResponseOrderDto update(Long id, ResponseOrderDto orderDto) {
        throw new UnsupportedOperationException("update method is not supported in OrderServiceImpl class");
    }

    @Override
    public ResponseOrderDto findById(Long id) {
        Order order = findOrderById(id);
        return orderMapper.orderToDto(order);
    }

    private Order findOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new SystemException(NON_EXISTENT_ENTITY));
    }

    @Override
    @Transactional
    public Page<ResponseOrderDto> findAll(Pageable pageable) {
        Page<Order> orderPage = orderRepository.findAll(pageable);
        if(!validator.isPageExists(pageable,orderPage.getTotalElements())){
            throw new SystemException(NON_EXISTENT_PAGE);
        }
        return new CustomPage<>(orderPage.getContent(), orderPage.getPageable(), orderPage.getTotalElements())
                .map(orderMapper::orderToDto);
    }

    @Override
    public void delete(Long id) {
        throw new UnsupportedOperationException("delete method is not supported in OrderServiceImpl class");
    }
}