package com.dune.greensupermarketbackend.order.service.impl;

import com.dune.greensupermarketbackend.cart.cart_item.dto.CartItemResponseDto;
import com.dune.greensupermarketbackend.cart.cart_item.service.CartItemService;
import com.dune.greensupermarketbackend.customer.CustomerEntity;
import com.dune.greensupermarketbackend.customer.CustomerRepository;
import com.dune.greensupermarketbackend.customer.address.AddressEntity;
import com.dune.greensupermarketbackend.customer.address.AddressRepository;
import com.dune.greensupermarketbackend.exception.APIException;
import com.dune.greensupermarketbackend.order.OrderDto;
import com.dune.greensupermarketbackend.order.OrderEntity;
import com.dune.greensupermarketbackend.order.OrderRepository;
import com.dune.greensupermarketbackend.order.order_item.OrderItemDto;
import com.dune.greensupermarketbackend.order.order_item.service.OrderItemService;
import com.dune.greensupermarketbackend.order.service.OrderService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;
    private final CartItemService cartItemService;
    private final OrderItemService orderItemService;


    public OrderServiceImpl(OrderRepository orderRepository, CustomerRepository customerRepository, AddressRepository addressRepository, ModelMapper modelMapper, CartItemService cartItemService, OrderItemService orderItemService) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
        this.modelMapper = modelMapper;
        this.cartItemService = cartItemService;
        this.orderItemService = orderItemService;
    }

    public AddressEntity checkAddress(Integer addressId){
        return addressRepository.findById(addressId)
                .orElseThrow(
                        ()-> new APIException(HttpStatus.NOT_FOUND,"Address not found with id "+addressId)
                );
    }

    public OrderEntity checkOrder(Integer orderId){
        return orderRepository.findById(orderId)
                .orElseThrow(
                        ()-> new APIException(HttpStatus.NOT_FOUND,"Order not found with id "+orderId)
                );
    }

    public CustomerEntity checkCustomer(Integer customerId){
        return customerRepository.findById(customerId)
                .orElseThrow(
                        ()-> new APIException(HttpStatus.NOT_FOUND,"Customer not found with id "+customerId)
                );
    }

    @Transactional
    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        OrderEntity order = modelMapper.map(orderDto,OrderEntity.class);

        CustomerEntity customer = checkCustomer(order.getCustomer().getId());

        order.setCustomer(customer);
        order.setBillingAddress(checkAddress(orderDto.getBillingAddressId()));
        order.setShippingAddress(checkAddress(orderDto.getShippingAddressId()));
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus("Received");
        order.setPaymentStatus("Pending");

        OrderEntity savedOrderEntity = orderRepository.save(order);

        Integer orderId = savedOrderEntity.getOrderId();

        List<CartItemResponseDto> cartItems = cartItemService.getCartItems(customer.getCart().getCartId());
        savedOrderEntity.setNumberOfItems(cartItems.size());

        AtomicReference<Double> totalAmount = new AtomicReference<>(0.0);

        cartItems.forEach(cartItem -> {
            OrderItemDto orderItemDto = orderItemService.create(orderId,cartItem);
            totalAmount.updateAndGet(v -> v + orderItemDto.getTotalAmount());
        });

        savedOrderEntity.setTotalAmount(totalAmount.get());

        orderRepository.save(savedOrderEntity);

        cartItemService.deleteAllCartItems(customer.getCart().getCartId());

        return modelMapper.map(order,OrderDto.class);
    }

    @Override
    public OrderDto updateOrderStatus(Integer orderId, String orderStatus) {

        OrderEntity order = checkOrder(orderId);

        order.setOrderStatus(orderStatus);

        return modelMapper.map(orderRepository.save(order),OrderDto.class);
    }

    @Override
    public OrderDto updatePaymentStatus(Integer orderId, String paymentStatus) {
        OrderEntity order = checkOrder(orderId);
        order.setPaymentStatus(paymentStatus);
        return modelMapper.map(orderRepository.save(order),OrderDto.class);
    }

    @Override
    public List<OrderDto> findByOrderStatus(String orderStatus) {
        List<OrderEntity> orders = orderRepository.findByOrderStatus(orderStatus);
        return orders.stream().map(order -> modelMapper.map(order,OrderDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> findByCustomerId(Integer customerId) {
        checkCustomer(customerId);

        List<OrderEntity> orders = orderRepository.findByCustomerIdOrderByOrderDateDesc(customerId);
        return orders.stream().map(order -> modelMapper.map(order,OrderDto.class)).collect(Collectors.toList());
    }
}
