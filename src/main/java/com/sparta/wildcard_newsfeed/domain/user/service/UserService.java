package com.sparta.wildcard_newsfeed.domain.user.service;

import com.sparta.wildcard_newsfeed.domain.user.dto.*;
import com.sparta.wildcard_newsfeed.domain.user.entity.User;
import com.sparta.wildcard_newsfeed.domain.user.entity.UserStatusEnum;
import com.sparta.wildcard_newsfeed.domain.user.repository.UserRepository;
import com.sparta.wildcard_newsfeed.exception.customexception.UserNotFoundException;
import com.sparta.wildcard_newsfeed.security.AuthenticationUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserSignupResponseDto signup(UserSignupRequestDto requestDto) {
        String usercode = requestDto.getUsercode();

        Optional<User> userExist = userRepository.findByUsercode(usercode);
        if(userExist.isPresent()) {
            throw new IllegalArgumentException("중복된 ID로는 회원가입 할 수 없습니다.");
        }

        String pwd = passwordEncoder.encode(requestDto.getPassword());
        //Bcrypt 암호화

        User user = userRepository.save(new User(usercode, pwd));

        return new UserSignupResponseDto(user);
    }

    @Transactional
    public UserSignupResponseDto resign(UserSignupRequestDto requestDto) {
        String usercode = requestDto.getUsercode();

        Optional<User> userExist = userRepository.findByUsercode(usercode);
        if(!userExist.isPresent())
            throw new NullPointerException("해당하는 회원이 없습니다!!");

        User user = userExist.get();

        if(user.getUserStatus() == UserStatusEnum.DISABLED)
            throw new IllegalArgumentException("이미 탈퇴한 사용자입니다!!");

        if(!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다!!");
        }

        user.setUserStatus(UserStatusEnum.DISABLED);

        return null;
    }

    @Transactional(readOnly = true)
    public UserResponseDto findById(Long userId) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return new UserResponseDto(findUser);
    }

    @Transactional
    public UserResponseDto updateUser(AuthenticationUser loginUser, Long userId, UserRequestDto requestDto) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (requestDto.getPassword() != null) {
            if (requestDto.getChangePassword() == null) {
                throw new IllegalArgumentException("변경할 비밀번호을 입력해 주세요.");
            }
        }
        if (requestDto.getChangePassword() != null) {
            if (requestDto.getPassword() == null) {
                throw new IllegalArgumentException("현재 비밀번호를 입력해 주세요.");
            }
        }
        if (requestDto.getPassword() != null && requestDto.getChangePassword() != null) {
            if (!loginUser.getPassword().equals(requestDto.getPassword()) || !findUser.getPassword().equals(requestDto.getPassword())) {
                throw new IllegalArgumentException("사용자가 일치하지 않습니다.");
            }
            if (requestDto.getPassword().equals(requestDto.getChangePassword())) {
                throw new IllegalArgumentException("변경하려는 비밀번호와 현재 비밀번호가 같습니다.");
            }
        }

        findUser.update(requestDto);

        User savedUser = userRepository.save(findUser);
        return new UserResponseDto(savedUser);
    }

    public UserResponseFromTokenDto findByUsercode(String usercode) {
        User user = userRepository.findByUsercode(usercode).orElseThrow(UserNotFoundException::new);
        return UserResponseFromTokenDto.of(user);
    }

    public void updateRefreshToken(String usercode, String refreshToken) {
        User user = userRepository.findByUsercode(usercode).orElseThrow(UserNotFoundException::new);
        user.setRefreshToken(refreshToken);
    }
}