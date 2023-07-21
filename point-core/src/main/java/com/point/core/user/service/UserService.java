package com.point.core.user.service;

import com.point.core.user.domain.User;

public interface UserService {

    User findUser(Long userId);

    User save(User user);
}
