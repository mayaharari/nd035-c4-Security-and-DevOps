package com.example.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserControllerTest {

  private UserController userController;
  private UserRepository userRepoMock = mock(UserRepository.class);
  private CartRepository cartRepoMock = mock(CartRepository.class);
  private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

  @Before
  public void setUp() throws Exception {
    userController = new UserController();
    TestUtils.injectObjects(userController, "userRepository", userRepoMock);
    TestUtils.injectObjects(userController, "cartRepository", cartRepoMock);
    TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
  }

  @Test
  public void findById() {
    when(encoder.encode("test123")).thenReturn("hashedPassword");
    User user = new User();
    user.setId(1);
    user.setUsername("test");
    user.setPassword(encoder.encode("test123"));
    when(userRepoMock.findById(1L)).thenReturn(java.util.Optional.of(user));
    ResponseEntity<User> findResponse = userController.findById(1L);
    assertNotNull(findResponse);
    assertEquals(HttpStatus.OK, findResponse.getStatusCode());
    User foundUser = findResponse.getBody();
    assertEquals(user, foundUser);
  }

  @Test
  public void findByUserName() {
    when(encoder.encode("test123")).thenReturn("hashedPassword");
    User user = new User();
    user.setId(1);
    user.setUsername("test");
    user.setPassword(encoder.encode("test123"));
    when(userRepoMock.findByUsername("test")).thenReturn(user);
    ResponseEntity<User> findResponse = userController.findByUserName("test");
    assertNotNull(findResponse);
    assertEquals(HttpStatus.OK, findResponse.getStatusCode());
    User foundUser = findResponse.getBody();
    assertEquals(user, foundUser);
  }

  @Test
  public void createUser_positive_thenOK() {
    when(encoder.encode("test123")).thenReturn("hashedPassword");
    CreateUserRequest createUser = new CreateUserRequest();
    createUser.setUsername("test");
    createUser.setPassword("test123");
    createUser.setConfirmPassword("test123");

    ResponseEntity<User> userResponse = userController.createUser(createUser);
    assertNotNull(userResponse);
    assertEquals(HttpStatus.OK, userResponse.getStatusCode());

    User user = userResponse.getBody();
    assertNotNull(user);
    assertEquals(0, user.getId());
    assertEquals("test", user.getUsername());
    assertEquals("hashedPassword", user.getPassword());
  }

  @Test
  public void createUser_negative_passwordLessThan7_thenError() {
    when(encoder.encode("pass")).thenReturn("hashedPassword");
    CreateUserRequest createUser = new CreateUserRequest();
    createUser.setUsername("test");
    createUser.setPassword("pass");
    createUser.setConfirmPassword("pass");

    ResponseEntity<User> userResponse = userController.createUser(createUser);
    assertNotNull(userResponse);
    assertEquals(HttpStatus.BAD_REQUEST, userResponse.getStatusCode());
  }

  @Test
  public void createUser_negative_differentConfirmPassword_thenError() {
    when(encoder.encode("test123")).thenReturn("hashedPassword");
    CreateUserRequest createUser = new CreateUserRequest();
    createUser.setUsername("test");
    createUser.setPassword("test123");
    createUser.setConfirmPassword("test12");

    ResponseEntity<User> userResponse = userController.createUser(createUser);
    assertNotNull(userResponse);
    assertEquals(HttpStatus.BAD_REQUEST, userResponse.getStatusCode());
  }
}