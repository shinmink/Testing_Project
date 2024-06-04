package com.sparta.wildcard_newsfeed.domain.user.service;

import com.sparta.wildcard_newsfeed.domain.user.dto.UserSignupRequestDto;
import com.sparta.wildcard_newsfeed.domain.user.dto.UserSignupResponseDto;
import com.sparta.wildcard_newsfeed.domain.user.entity.User;
import com.sparta.wildcard_newsfeed.domain.user.entity.UserStatusEnum;
import com.sparta.wildcard_newsfeed.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserSignupResponseDto signup(UserSignupRequestDto requestDto) {
        String usercode = requestDto.getUsercode();

        Optional<User> userExist = userRepository.findByUsercode(usercode);
        if(userExist.isPresent()) {
            throw new IllegalArgumentException("중복된 ID로는 회원가입 할 수 없습니다.");
        }

        String pwd = requestDto.getPassword();
        //Bcrypt 암호화
        //passwordEncoder.encode()

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

        String pwd = requestDto.getPassword();
        //passwordEncoder.encode(

        if(!user.getPassword().equals(pwd)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다!!");
        }

        user.setUserStatus(UserStatusEnum.DISABLED);

        return null;
    }
}