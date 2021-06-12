package com.example.demo.controllers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class OrderControllerTest {

  private OrderController orderController;
  private UserRepository userRepoMock = mock(UserRepository.class);
  private OrderRepository orderRepoMock = mock(OrderRepository.class);
  private User user = new User();
  private Item item = new Item();
  private UserOrder userOrder = new UserOrder();

  @Before
  public void setUp() throws Exception {
    orderController = new OrderController();
    TestUtils.injectObjects(orderController, "userRepository", userRepoMock);
    TestUtils.injectObjects(orderController, "orderRepository", orderRepoMock);
    user.setId(1);
    user.setUsername("test");
    user.setPassword("test123");
    user.setCart(new Cart());
    item.setId(1L);
    item.setDescription("item1Desc");
    item.setName("item1");
    item.setPrice(BigDecimal.valueOf(1.50));
    user.getCart().setId(1L);
    user.getCart().setUser(user);
    user.getCart().addItem(item);
    userOrder.setUser(user);
    userOrder.setId(1L);
    userOrder.setItems(Arrays.asList(item));
  }

  @Test
  public void submit_positive_ThenOK() {
    when(userRepoMock.findByUsername("test")).thenReturn(user);
    final ResponseEntity<UserOrder> response = orderController.submit("test");
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(1, response.getBody().getItems().size());
    assertEquals(item, response.getBody().getItems().get(0));
  }

  @Test
  public void submit_negative_UserNotFound_ThenError() {
    when(userRepoMock.findByUsername("test")).thenReturn(user);
    final ResponseEntity<UserOrder> response = orderController.submit("test1");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void getOrdersForUser_positive_ThenOK() {
    when(userRepoMock.findByUsername("test")).thenReturn(user);
    when(orderRepoMock.findByUser(user)).thenReturn(Arrays.asList(userOrder));
    final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(1, response.getBody().size());
    assertEquals(userOrder, response.getBody().get(0));
  }

  @Test
  public void getOrdersForUser_negative_UserNotFound_ThenError() {
    when(userRepoMock.findByUsername("test")).thenReturn(user);
    when(orderRepoMock.findByUser(user)).thenReturn(Arrays.asList(userOrder));
    final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test1");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

}