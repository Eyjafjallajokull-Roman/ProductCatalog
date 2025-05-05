package org.example.productcategory.service;

import org.example.productcategory.entity.AppUser;

public interface UserService {

  String register(AppUser user);

  String login(AppUser user);
}