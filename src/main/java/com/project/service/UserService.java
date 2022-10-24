package com.project.service;

import com.project.entity.UserEntity;
import com.project.request.UserRequest;
import com.project.response.UserResponse;

import java.util.Optional;

public interface UserService extends BaseService<UserResponse,UserRequest>{

    Optional<UserEntity> findById(Long id);

    UserEntity findByUserName(String userName);

    UserResponse updateUserInfo(UserRequest request);

    UserResponse getCurrent();

    UserResponse changePassword(UserRequest request);

    UserResponse getAllUserNoneDeleted(Integer page, Integer size);
}
