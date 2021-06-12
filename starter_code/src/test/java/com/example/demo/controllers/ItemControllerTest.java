package com.example.demo.controllers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ItemControllerTest {

  private ItemController itemController;
  private ItemRepository itemRepoMock = mock(ItemRepository.class);
  Item item = new Item();
  Item item1 = new Item();
  List<Item> items = new ArrayList<>();

  @Before
  public void setUp() throws Exception {
    itemController = new ItemController();
    TestUtils.injectObjects(itemController, "itemRepository", itemRepoMock);
    item.setId(1L);
    item.setName("test");
    item.setPrice(BigDecimal.valueOf(2.20));
    item.setDescription("test item");
    item1.setId(2L);
    item1.setName("test1");
    item1.setPrice(BigDecimal.valueOf(150.80));
    item1.setDescription("test item1");
    items = Arrays.asList(item, item1);
  }

  @Test
  public void getItems() {
    when(itemRepoMock.findAll()).thenReturn(items);
    final ResponseEntity<List<Item>> getResponse = itemController.getItems();
    assertNotNull(getResponse);
    assertEquals(HttpStatus.OK, getResponse.getStatusCode());
    assertEquals(2, getResponse.getBody().size());
    assertEquals(items, getResponse.getBody());
  }

  @Test
  public void getItemById() {
    when(itemRepoMock.findById(1L)).thenReturn(java.util.Optional.ofNullable(item));
    final ResponseEntity<Item> getResponse = itemController.getItemById(1L);
    assertNotNull(getResponse);
    assertEquals(HttpStatus.OK, getResponse.getStatusCode());
    assertEquals(item, getResponse.getBody());
  }

  @Test
  public void getItemsByName() {
    when(itemRepoMock.findByName("test1")).thenReturn(Arrays.asList(item1));
    final ResponseEntity<List<Item>> getResponse = itemController.getItemsByName("test1");
    assertNotNull(getResponse);
    assertEquals(HttpStatus.OK, getResponse.getStatusCode());
    assertEquals(1, getResponse.getBody().size());
    assertEquals(item1, getResponse.getBody().get(0));
  }
}