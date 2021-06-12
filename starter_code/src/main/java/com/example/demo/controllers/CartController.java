package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import java.util.Optional;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
public class CartController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private ItemRepository itemRepository;

	public static final Logger log = LoggerFactory.getLogger(CartController.class);


	@PostMapping("/addToCart")
	public ResponseEntity<Cart> addToCart(@RequestBody ModifyCartRequest request) {
		log.info("Going to add new items to cart");
		User user = userRepository.findByUsername(request.getUsername());
		if(user == null) {
			log.error("The following user name was not found: {}",request.getUsername());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		log.debug("Going to find item id: {}", request.getItemId());
		Optional<Item> item = itemRepository.findById(request.getItemId());
		if(!item.isPresent()) {
			log.error("Item with requested ID {} was not found", request.getItemId());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Cart cart = user.getCart();
		IntStream.range(0, request.getQuantity())
			.forEach(i -> {
				log.info("going to add item id {} to the cart", item.get().getId());
				cart.addItem(item.get());
			});
		cartRepository.save(cart);
		return ResponseEntity.ok(cart);
	}
	
	@PostMapping("/removeFromCart")
	public ResponseEntity<Cart> removeFromCart(@RequestBody ModifyCartRequest request) {
		User user = userRepository.findByUsername(request.getUsername());
		if(user == null) {
			log.error("The following user name was not found: {}",request.getUsername());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		log.debug("Going to find item id: {}", request.getItemId());
		Optional<Item> item = itemRepository.findById(request.getItemId());
		if(!item.isPresent()) {
			log.error("Item with requested ID {} was not found", request.getItemId());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Cart cart = user.getCart();
		IntStream.range(0, request.getQuantity())
			.forEach(i -> {
				log.info("going to remove item id {} from the cart", item.get().getId());
				cart.removeItem(item.get());
			});
		cartRepository.save(cart);
		return ResponseEntity.ok(cart);
	}
		
}
