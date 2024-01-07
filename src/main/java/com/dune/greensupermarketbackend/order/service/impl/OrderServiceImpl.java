package com.dune.greensupermarketbackend.order.service.impl;

import com.dune.greensupermarketbackend.cart.dto.CartWithItemsDto;
import com.dune.greensupermarketbackend.cart.cart_item.dto.CartItemResponseDto;
import com.dune.greensupermarketbackend.cart.cart_item.service.CartItemService;
import com.dune.greensupermarketbackend.cart.service.CartService;
import com.dune.greensupermarketbackend.customer.CustomerEntity;
import com.dune.greensupermarketbackend.customer.CustomerRepository;
import com.dune.greensupermarketbackend.customer.address.AddressDto;
import com.dune.greensupermarketbackend.customer.address.AddressEntity;
import com.dune.greensupermarketbackend.customer.address.AddressRepository;
import com.dune.greensupermarketbackend.exception.APIException;
import com.dune.greensupermarketbackend.mail.dto.EmailData;
import com.dune.greensupermarketbackend.mail.service.MailService;
import com.dune.greensupermarketbackend.order.dto.OrderDto;
import com.dune.greensupermarketbackend.order.OrderEntity;
import com.dune.greensupermarketbackend.order.OrderRepository;
import com.dune.greensupermarketbackend.order.dto.OrderResponseDto;
import com.dune.greensupermarketbackend.order.dto.OrderWithItemsDto;
import com.dune.greensupermarketbackend.order.order_item.OrderItemDto;
import com.dune.greensupermarketbackend.order.order_item.OrderItemEntity;
import com.dune.greensupermarketbackend.order.order_item.OrderItemRepository;
import com.dune.greensupermarketbackend.order.order_item.service.OrderItemService;
import com.dune.greensupermarketbackend.order.service.OrderService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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
    private final OrderItemRepository orderItemRepository;
    private final MailService mailService;

    public OrderServiceImpl(OrderRepository orderRepository, CustomerRepository customerRepository, AddressRepository addressRepository, ModelMapper modelMapper, CartItemService cartItemService, OrderItemService orderItemService, CartService cartService, OrderItemRepository orderItemRepository, MailService mailService) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
        this.modelMapper = modelMapper;
        this.cartItemService = cartItemService;
        this.orderItemService = orderItemService;
        this.cartService = cartService;
        this.orderItemRepository = orderItemRepository;
        this.mailService = mailService;
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
    public OrderResponseDto createOrder(OrderDto orderDto) {

        OrderEntity lastOrder = orderRepository.findLastOrderByCustomerId(orderDto.getCustomerId())
                .orElse(null);

        if (lastOrder != null && !"Received".equals(lastOrder.getPaymentStatus())) {

            lastOrder.getOrderItems().forEach(orderItem -> {
                orderItem.getProduct().setStockAvailableUnits(orderItem.getProduct().getStockAvailableUnits() + orderItem.getQuantity());
                orderItemRepository.save(orderItem);
            });

            lastOrder.setPaymentStatus("Fail");
            lastOrder.setOrderStatus("Payment Failed");
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

        OrderResponseDto orderDtoResponse = modelMapper.map(order,OrderResponseDto.class);
        orderDtoResponse.setOrderItems(orderItems);
        orderDtoResponse.setBillingAddress(modelMapper.map(order.getBillingAddress(), AddressDto.class));
        orderDtoResponse.setShippingAddress(modelMapper.map(order.getShippingAddress(), AddressDto.class));

        return orderDtoResponse;
    }

    @Override
    public OrderResponseDto updateOrderStatus(Integer orderId, OrderDto orderDto) {

        OrderEntity order = checkOrder(orderId);

        order.setOrderStatus(orderDto.getOrderStatus());

        OrderResponseDto orderDto1 =  modelMapper.map(orderMapper(orderRepository.save(order)),OrderResponseDto.class);
        orderDto1.setBillingAddress(modelMapper.map(order.getBillingAddress(),AddressDto.class));
        orderDto1.setShippingAddress(modelMapper.map(order.getShippingAddress(),AddressDto.class));

        return orderDto1;

    }

    @Override
    public OrderResponseDto updatePaymentStatus(Integer orderId, OrderDto orderDto) {
        OrderEntity order = checkOrder(orderId);
        order.setPaymentStatus(orderDto.getPaymentStatus());
        OrderResponseDto orderDto1 =  modelMapper.map(orderMapper(orderRepository.save(order)),OrderResponseDto.class);
        orderDto1.setBillingAddress(modelMapper.map(order.getBillingAddress(),AddressDto.class));
        orderDto1.setShippingAddress(modelMapper.map(order.getShippingAddress(),AddressDto.class));

        return orderDto1;
    }

    @Override
    public List<OrderResponseDto> findByOrderStatus(String orderStatus) {
        List<OrderEntity> orders = orderRepository.findByOrderStatus(orderStatus);

        return orders.stream().map(order->{
            OrderResponseDto orderResponseDto = modelMapper.map(orderMapper(order),OrderResponseDto.class);
            orderResponseDto.setBillingAddress(modelMapper.map(order.getBillingAddress(),AddressDto.class));
            orderResponseDto.setShippingAddress(modelMapper.map(order.getShippingAddress(),AddressDto.class));
            return orderResponseDto;
        }
        ).collect(Collectors.toList());
    }

    @Override
    public List<OrderResponseDto> findByCustomerId(Integer customerId) {
        checkCustomer(customerId);

        List<OrderEntity> orders = orderRepository.findByCustomerIdOrderByOrderDateDesc(customerId);
        return orders.stream().map(order->{
                    OrderResponseDto orderResponseDto = modelMapper.map(orderMapper(order),OrderResponseDto.class);
                    orderResponseDto.setBillingAddress(modelMapper.map(order.getBillingAddress(),AddressDto.class));
                    orderResponseDto.setShippingAddress(modelMapper.map(order.getShippingAddress(),AddressDto.class));
                    orderResponseDto.setNote(order.getNote());

                    List<OrderItemDto> orderItemDtos = orderItemService.getByOrderId(order.getOrderId());

                    orderResponseDto.setOrderItems(orderItemDtos);
                    return orderResponseDto;
                }
        ).collect(Collectors.toList());
    }

    @Override
    public OrderResponseDto payementSuccess(Integer orderId) {
        OrderEntity order = checkOrder(orderId);
        order.setPaymentStatus("Success");
        order.setOrderStatus("Received");

        orderRepository.save(order);

        cartItemService.deleteAllCartItems(order.getCustomer().getCart().getCartId());

        OrderResponseDto orderDto1 =  modelMapper.map(orderMapper(orderRepository.save(order)),OrderResponseDto.class);
        orderDto1.setBillingAddress(modelMapper.map(order.getBillingAddress(),AddressDto.class));
        orderDto1.setShippingAddress(modelMapper.map(order.getShippingAddress(),AddressDto.class));

        orderDto1.setNote(order.getNote());

        List<OrderItemDto> orderItemDtos = orderItemService.getByOrderId(order.getOrderId());

        orderDto1.setOrderItems(orderItemDtos);

        StringBuilder tableRows = new StringBuilder();
        for (OrderItemDto item : orderDto1.getOrderItems()) {
            tableRows.append(String.format(
                    "<tr><td>%s</td><td>LKR %.2f</td><td>%d</td><td>LKR %.2f</td></tr>",
                    item.getProductName(), item.getPrice()- item.getDiscount()/ item.getQuantity(), item.getQuantity(), (item.getPrice()- item.getDiscount()/ item.getQuantity()) * item.getQuantity()
            ));
        }

        String htmlBody =
                "<!DOCTYPE html>" +
                        "<html>" +
                        "<head>" +
                        "<link href='https://fonts.googleapis.com/css2?family=Poppins&display=swap' rel='stylesheet'>" +
                        "<style>" +
                        "body { font-family: \"Poppins\", sans-serif; }" +
                        "h3 { color: #191919; }" +
                        "p { color: rgb(71, 71, 71); }" +
                        "table { font-size: 14px; }" +
                        "th { border: solid 1px rgb(194, 194, 194); text-align: left; font-weight: 500; padding-top: 16px; padding-bottom: 16px; padding-left: 20px; padding-right: 20px; color: #00b207; }" +
                        "td { border: solid 1px rgb(194, 194, 194); text-align: left; font-weight: 400; padding-left: 20px; padding-right: 20px; padding-top: 8px; padding-bottom: 8px; }" +
                        "</style>" +
                        "</head>" +
                        "<body>" +
                        "<p style=\"padding-top: 40px;\"><strong>Hi " + order.getCustomer().getFirstname() + " " + order.getCustomer().getLastname() + ",</strong></p>" +
                        "<p>Your order has been placed successfully. Your order id is <strong>" + order.getOrderId() + "</strong>.</p>" +
                        "<p>You can view your order details in your account.</br><a href='https://shop.green-supermarket.com/order-history'>View order details</a></p>" +
                        "<br />" +
                        "<table>" +
                        "<tr><th>Product Name</th><th>Unit Price</th><th>Quantity</th><th>Total Price</th></tr>" +
                        tableRows.toString() +
                        "<tr><td colspan=\"3\" style=\"font-weight: 600;\">Shipping</td><td style=\"font-weight: 600;\">LKR 0.00</td></tr>" +
                        "<tr><td colspan=\"3\" style=\"font-weight: 600; color: #00b207;\">Total</td><td style=\"font-weight: 600; color: #00b207;\">LKR " + order.getTotalAmount() + "</td></tr>" +
                        "</table>" +
                        "<br />" +
                        "<p>Thank you.</p>" +
                        "<p><a href='https://shop.green-supermarket.com'><span style=\"color: #00b207; font-weight: 500\">GREEN </span><span style=\"color: black; font-weight: 500\">SUPERMARKET</span></a></p>" +
                        "</body>" +
                        "</html>";



        EmailData emailData = new EmailData(
        order.getCustomer().getEmail(),
        order.getCustomer().getEmail(),
        "Order Successful",
        htmlBody
        );

        mailService.sendMail(emailData);

        return orderDto1;
    }

    @Override
    public List<OrderResponseDto> getAllOrders() {
        List<OrderEntity> orders = orderRepository.findAllByOrderByOrderDateDesc();
        return orders.stream().map(order->{
                    OrderResponseDto orderResponseDto = modelMapper.map(orderMapper(order),OrderResponseDto.class);
                    orderResponseDto.setBillingAddress(modelMapper.map(order.getBillingAddress(),AddressDto.class));
                    orderResponseDto.setShippingAddress(modelMapper.map(order.getShippingAddress(),AddressDto.class));

                    orderResponseDto.setNote(order.getNote());

                    List<OrderItemDto> orderItemDtos = orderItemService.getByOrderId(order.getOrderId());

                    orderResponseDto.setOrderItems(orderItemDtos);

                    return orderResponseDto;
                }
        ).collect(Collectors.toList());
    }

    @Override
    public OrderResponseDto getOrderWithItems(Integer orderId) {
        OrderEntity order = checkOrder(orderId);

        OrderDto orderDto = orderMapper(checkOrder(orderId));
        OrderResponseDto orderResponseDto = modelMapper.map(orderDto, OrderResponseDto.class);
        orderResponseDto.setBillingAddress(modelMapper.map(order.getBillingAddress(),AddressDto.class));
        orderResponseDto.setShippingAddress(modelMapper.map(order.getShippingAddress(),AddressDto.class));
        orderResponseDto.setNote(order.getNote());

        List<OrderItemDto> orderItemDtos = orderItemService.getByOrderId(orderId);

        orderResponseDto.setOrderItems(orderItemDtos);
        return orderResponseDto;
    }
}
