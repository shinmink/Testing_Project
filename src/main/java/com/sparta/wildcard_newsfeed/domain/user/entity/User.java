package com.sparta.wildcard_newsfeed.domain.user.entity;

import com.sparta.wildcard_newsfeed.domain.common.TimeStampEntity;
import com.sparta.wildcard_newsfeed.domain.user.dto.UserRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends TimeStampEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String usercode; // 사용자 ID
    private String password;
    private String name; // 이름
    private String email;
    private String introduce;
    @Setter
    private String refreshToken;
    @Enumerated(EnumType.STRING)
    private UserStatusEnum userStatus; //회원상태코드
    private LocalDateTime authUserAt; //상태 변경 시간

    @Enumerated(EnumType.STRING)
    private UserRoleEnum userRoleEnum;

    @Builder
    public User(String usercode, String password, UserRoleEnum userRoleEnum) {
        this.usercode = usercode;
        this.password = password;
        this.userRoleEnum = userRoleEnum;
    }

    /** 가입 시 사용 **/
    public User(String usercode, String password){
        this.usercode = usercode;
        this.password = password;
        this.name = usercode;
        //유저 이름의 기본 값은 사용자 ID
        this.userStatus = UserStatusEnum.ENABLED;
        this.authUserAt = LocalDateTime.now();
        //가입 시 회원상태코드는 정상과 상태 변경 시간 적용
    }

    /** 회원 탈퇴 시 사용 **/
    public void setUserStatus(UserStatusEnum userStatus){
        this.userStatus = userStatus;
        this.authUserAt = LocalDateTime.now();
    }

    public void update(UserRequestDto requestDto) {
        this.password = requestDto.getChangePassword() != null ? requestDto.getChangePassword() : this.password;
        this.name = requestDto.getName() != null ? requestDto.getName() : this.name;
        this.email = requestDto.getEmail() != null ? requestDto.getEmail() : this.email;
        this.introduce = requestDto.getIntroduce() != null ? requestDto.getIntroduce() : this.introduce;
    }
}