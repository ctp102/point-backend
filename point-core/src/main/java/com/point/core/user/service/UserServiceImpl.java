package com.point.core.user.service;

import com.point.core.common.exception.CustomNotFoundException;
import com.point.core.user.domain.User;
import com.point.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.point.core.common.enums.ErrorResponseCodes.USER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public User findUser(Long userId) {
        User user = userRepository.findById(userId).orElse(null);

        if (Objects.isNull(user)) {
            log.error("[findUser] 사용자를 찾을 수 없습니다. userId: {}", userId);
            throw new CustomNotFoundException(USER_NOT_FOUND.getNumber(), USER_NOT_FOUND.getMessage());
        }
        return user;
    }

    @Transactional
    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

}
