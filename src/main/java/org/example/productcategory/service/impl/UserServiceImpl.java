package org.example.productcategory.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.productcategory.entity.AppUser;
import org.example.productcategory.exception.BadRequestException;
import org.example.productcategory.repository.UserRepository;
import org.example.productcategory.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final LoginAttemptService loginAttemptService;

  @Override
  public String register(AppUser user) {
    if (userRepository.existsByUsername(user.getUsername())) {
      throw new BadRequestException("Username already exists");
    }

    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setRole("ROLE_USER");
    userRepository.save(user);
    return "User registered successfully";
  }

  @Override
  public String login(AppUser request) {
    if (loginAttemptService.isBlocked(request.getUsername())) {
      throw new BadRequestException("Too many login attempts. Try again later.");
    }

    AppUser user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new BadRequestException("Invalid credentials"));

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      loginAttemptService.loginFailed(request.getUsername());
      throw new BadRequestException("Invalid password");
    }

    loginAttemptService.loginSucceeded(request.getUsername());

    return jwtService.generateToken(user.getUsername(), user.getRole());
  }
}