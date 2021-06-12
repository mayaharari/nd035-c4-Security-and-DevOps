package com.example.demo.controllers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import java.math.BigDecimal;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CartControllerTest {

  private CartController cartController;
  private UserRepository userRepoMock = mock(UserRepository.class);
  private CartRepository cartRepoMock = mock(CartRepository.class);
  private ItemRepository itemRepoMock = mock(ItemRepository.class);

  private User user = new User();
  private Item item = new Item();

  @Before
  public void setUp() throws Exception {
    cartController = new CartController();
    TestUtils.injectObjects(cartController, "userRepository", userRepoMock);
    TestUtils.injectObjects(cartController, "cartRepository", cartRepoMock);
    TestUtils.injectObjects(cartController, "itemRepository", itemRepoMock);
    user.setId(1);
    user.setUsername("test");
    user.setPassword("test123");
    user.setCart(new Cart());
    item.setId(1L);
    item.setDescription("item1Desc");
    item.setName("item1");
    item.setPrice(BigDecimal.valueOf(1.50));
  }

  @Test
  public void addToCart_positive_thenOK() {
    when(userRepoMock.findByUsername("test")).thenReturn(user);
    when(itemRepoMock.findById(1L)).thenReturn(java.util.Optional.ofNullable(item));
    ModifyCartRequest request = new ModifyCartRequest();
    request.setUsername("test");
    request.setItemId(1L);
    request.setQuantity(1);

    final ResponseEntity<Cart> cartResponse = cartController.addToCart(request);
    assertNotNull(cartResponse);
    assertEquals(HttpStatus.OK, cartResponse.getStatusCode());

    Cart updatedCart = cartResponse.getBody();
    assertNotNull(updatedCart);
    assertEquals(1, updatedCart.getItems().size());
    assertEquals(item, updatedCart.getItems().get(0));
    assertEquals(BigDecimal.valueOf(1.50),updatedCart.getTotal());
  }

  @Test
  public void addToCart_Negative_UserNotFound_thenError() {
    when(userRepoMock.findByUsername("test1")).thenReturn(user);
    when(itemRepoMock.findById(1L)).thenReturn(java.util.Optional.ofNullable(item));
    ModifyCartRequest request = new ModifyCartRequest();
    request.setUsername("test");
    request.setItemId(1L);
    request.setQuantity(1);

    final ResponseEntity<Cart> cartResponse = cartController.addToCart(request);
    assertEquals(HttpStatus.NOT_FOUND, cartResponse.getStatusCode());

  }

  @Test
  public void addToCart_Negative_ItemNotFound_thenError() {
    when(userRepoMock.findByUsername("test")).thenReturn(user);
    when(itemRepoMock.findById(2L)).thenReturn(java.util.Optional.ofNullable(item));
    ModifyCartRequest request = new ModifyCartRequest();
    request.setUsername("test");
    request.setItemId(1L);
    request.setQuantity(1);

    final ResponseEntity<Cart> cartResponse = cartController.addToCart(request);
    assertEquals(HttpStatus.NOT_FOUND, cartResponse.getStatusCode());

  }

  @Test
  public void removeFromCart_Positive_ThenOK() {
    when(userRepoMock.findByUsername("test")).thenReturn(user);
    when(itemRepoMock.findById(1L)).thenReturn(java.util.Optional.ofNullable(item));
    Cart cart = user.getCart();
    cart.setId(1L);
    cart.addItem(item);
    cart.setUser(user);
    ModifyCartRequest request = new ModifyCartRequest();
    request.setUsername("test");
    request.setItemId(1L);
    request.setQuantity(1);

    final ResponseEntity<Cart> cartResponse = cartController.removeFromCart(request);
    assertNotNull(cartResponse);
    assertEquals(HttpStatus.OK, cartResponse.getStatusCode());

    Cart updatedCart = cartResponse.getBody();
    assertNotNull(updatedCart);
    assertEquals(0, updatedCart.getItems().size());
    assertEquals(BigDecimal.valueOf(0.0), updatedCart.getTotal());
  }

  @Test
  public void removeFromCart_Negative_UserNotFound_thenError() {
    when(userRepoMock.findByUsername("test1")).thenReturn(user);
    when(itemRepoMock.findById(1L)).thenReturn(java.util.Optional.ofNullable(item));
    Cart cart = user.getCart();
    cart.setId(1L);
    cart.addItem(item);
    cart.setUser(user);
    ModifyCartRequest request = new ModifyCartRequest();
    request.setUsername("test");
    request.setItemId(1L);
    request.setQuantity(1);

    final ResponseEntity<Cart> cartResponse = cartController.addToCart(request);
    assertEquals(HttpStatus.NOT_FOUND, cartResponse.getStatusCode());

  }

  @Test
  public void removeFromCart_Negative_ItemNotFound_thenError() {
    when(userRepoMock.findByUsername("test")).thenReturn(user);
    when(itemRepoMock.findById(2L)).thenReturn(java.util.Optional.ofNullable(item));
    Cart cart = user.getCart();
    cart.setId(1L);
    cart.addItem(item);
    cart.setUser(user);
    ModifyCartRequest request = new ModifyCartRequest();
    request.setUsername("test");
    request.setItemId(1L);
    request.setQuantity(1);

    final ResponseEntity<Cart> cartResponse = cartController.addToCart(request);
    assertEquals(HttpStatus.NOT_FOUND, cartResponse.getStatusCode());

  }
}