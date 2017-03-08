package com.nuptsast.service;

import com.nuptsast.model.User;

public interface UserService {
    User register(User user);

    User getUser(String username);

    User updateUser(User user);
}
