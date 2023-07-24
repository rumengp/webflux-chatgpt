package com.anii.querydsl.mapper;

import com.anii.querydsl.common.UserContext;
import com.anii.querydsl.entity.User;
import com.anii.querydsl.request.UserRegisterReq;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper USER_MAPPER = Mappers.getMapper(UserMapper.class);

    User toDo(UserRegisterReq req);

    UserContext toContext(User user);
}
