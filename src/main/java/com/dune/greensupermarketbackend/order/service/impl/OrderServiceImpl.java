package com.dune.greensupermarketbackend.order.service.impl;

import com.dune.greensupermarketbackend.cart.CartWithItemsDto;
import com.dune.greensupermarketbackend.cart.cart_item.dto.CartItemResponseDto;
import com.dune.greensupermarketbackend.cart.cart_item.service.CartItemService;
import com.dune.greensupermarketbackend.cart.service.CartService;
import com.dune.greensupermarketbackend.customer.CustomerEntity;
import com.dune.greensupermarketbackend.customer.CustomerRepository;
import com.dune.greensupermarketbackend.customer.address.AddressEntity;
import com.dune.greensupermarketbackend.customer.address.AddressRepository;
import com.dune.greensupermarketbackend.exception.APIException;
import com.dune.greensupermarketbackend.order.OrderDto;
import com.dune.greensupermarketbackend.order.OrderEntity;
import com.dune.greensupermarketbackend.order.OrderRepository;
import com.dune.greensupermarketbackend.order.order_item.OrderItemDto;
import com.dune.greensupermarketbackend.order.order_item.OrderItemEntity;
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
    private final CartService cartService;

    public OrderServiceImpl(OrderRepository orderRepository, CustomerRepository customerRepository, AddressRepository addressRepository, ModelMapper modelMapper, CartItemService cartItemService, OrderItemService orderItemService, CartService cartService) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
        this.modelMapper = modelMapper;
        this.cartItemService = cartItemService;
        this.orderItemService = orderItemService;
        this.cartService = cartService;
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

    private OrderItemDto orderItemMapper(OrderItemEntity orderItemEntity){
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setOrderItemId(orderItemEntity.getOrderItemId());
        orderItemDto.setOrderId(orderItemEntity.getOrder().getOrderId());
        orderItemDto.setProductId(orderItemEntity.getProduct().getProductId());
        orderItemDto.setQuantity(orderItemEntity.getQuantity());
        orderItemDto.setPrice(orderItemEntity.getPrice());
        orderItemDto.setDiscount(orderItemEntity.getDiscount());
        orderItemDto.setTotalAmount(orderItemEntity.getTotalAmount());

        return orderItemDto;
    }

    private OrderDto orderMapper(OrderEntity orderEntity){
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId(orderEntity.getOrderId());
        orderDto.setCustomerId(orderEntity.getCustomer().getId());
        orderDto.setOrderDate(orderEntity.getOrderDate().toString());
        orderDto.setShippingFee(orderEntity.getShippingFee());
        orderDto.setDiscount(orderEntity.getDiscount());
        orderDto.setNumberOfItems(orderEntity.getNumberOfItems());
        orderDto.setTotalAmount(orderEntity.getTotalAmount());
        orderDto.setOrderStatus(orderEntity.getOrderStatus());
        orderDto.setPaymentMode(orderEntity.getPaymentMode());
        orderDto.setPaymentStatus(orderEntity.getPaymentStatus());
        orderDto.setNote(orderEntity.getNote());
        orderDto.setBillingAddressId(orderEntity.getBillingAddress().getId());
        orderDto.setShippingAddressId(orderEntity.getShippingAddress().getId());
        orderDto.setOrderItems(orderEntity.getOrderItems().stream().map(this::orderItemMapper).toList());

        return orderDto;
    }

    @Transactional
    @Override
    public OrderDto createOrder(OrderDto orderDto) {

        OrderEntity lastOrder = orderRepository.findLastOrderByCustomerId(orderDto.getCustomerId())
                .orElse(null);

        if (lastOrder != null && !"Received".equals(lastOrder.getPaymentStatus())) {
            lastOrder.setPaymentStatus("Fail");
            orderRepository.save(lastOrder);
        }

        OrderEntity order = modelMapper.map(orderDto,OrderEntity.class);

        CustomerEntity customer = checkCustomer(order.getCustomer().getId());

        order.setCustomer(customer);
        order.setBillingAddress(checkAddress(orderDto.getBillingAddressId()));
        order.setShippingAddress(checkAddress(orderDto.getShippingAddressId()));
        order.setOrderDate(LocalDateTime.now());
        order.setShippingFee(order.getShippingFee());
        order.setDiscount(order.getDiscount());
        order.setPaymentMode(order.getPaymentMode());
        order.setNote(order.getNote());
        order.setOrderStatus("Payment Pending");
        order.setPaymentStatus("Pending");

        OrderEntity savedOrderEntity = orderRepository.save(order);

        CartWithItemsDto cart = cartService.getCartWithItems(customer.getCart().getCartId());

        List<CartItemResponseDto> cartItems = cart.getCartItems();

        List<OrderItemDto> orderItems = cartItems.stream()
                .map(cartItem -> orderItemService.create(savedOrderEntity.getOrderId(), cartItem)).collect(Collectors.toList()
                );

        savedOrderEntity.setNumberOfItems(cart.getNumberOfItems());

        savedOrderEntity.setTotalAmount(cart.getTotalAmount());

        orderRepository.save(savedOrderEntity);

        OrderDto orderDtoResponse = modelMapper.map(order,OrderDto.class);
        orderDtoResponse.setOrderItems(orderItems);

        return orderDtoResponse;
    }

    @Override
    public OrderDto updateOrderStatus(Integer orderId, OrderDto orderDto) {

        OrderEntity order = checkOrder(orderId);

        order.setOrderStatus(orderDto.getOrderStatus());

        return orderMapper(orderRepository.save(order));
    }

    @Override
    public OrderDto updatePaymentStatus(Integer orderId, OrderDto orderDto) {
        OrderEntity order = checkOrder(orderId);
        order.setPaymentStatus(orderDto.getPaymentStatus());
        return orderMapper(orderRepository.save(order));
    }

    @Override
    public List<OrderDto> findByOrderStatus(String orderStatus) {
        List<OrderEntity> orders = orderRepository.findByOrderStatus(orderStatus);
        return orders.stream().map(this::orderMapper).collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> findByCustomerId(Integer customerId) {
        checkCustomer(customerId);

        List<OrderEntity> orders = orderRepository.findByCustomerIdOrderByOrderDateDesc(customerId);
        return orders.stream().map(this::orderMapper).collect(Collectors.toList());
    }

    @Override
    public OrderDto payementSuccess(Integer orderId) {
        OrderEntity order = checkOrder(orderId);
        order.setPaymentStatus("Success");
        order.setOrderStatus("Received");

        orderRepository.save(order);

        cartItemService.deleteAllCartItems(order.getCustomer().getCart().getCartId());

        return orderMapper(order);
    }

    @Override
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::orderMapper).collect(Collectors.toList()
                );
    }
}
