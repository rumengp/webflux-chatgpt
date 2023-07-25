package com.anii.querydsl.mapper;

import com.anii.querydsl.entity.ChatRole;
import com.anii.querydsl.request.chat.role.ChatRoleCreateRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ChatRoleMapper {
    ChatRoleMapper MAPPER = Mappers.getMapper(ChatRoleMapper.class);

    ChatRole toDo(ChatRoleCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copyProperties(ChatRoleCreateRequest request, @MappingTarget ChatRole chatRole);
}
