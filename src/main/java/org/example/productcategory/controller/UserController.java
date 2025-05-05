package org.example.productcategory.controller;

import lombok.RequiredArgsConstructor;
import org.example.productcategory.entity.AppUser;
import org.example.productcategory.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody AppUser user) {
    String result = userService.register(user);
    return ResponseEntity.ok(result);
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody AppUser user) {
    String token = userService.login(user);
    return ResponseEntity.ok(Map.of("token", token));
  }
}