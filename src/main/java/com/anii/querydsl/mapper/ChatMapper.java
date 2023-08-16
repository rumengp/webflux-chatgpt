package com.anii.querydsl.mapper;

import com.anii.querydsl.entity.Chat;
import com.anii.querydsl.entity.ChatRole;
import com.anii.querydsl.request.chat.ChatUpdateRequest;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ChatMapper {

    ChatMapper MAPPER = Mappers.getMapper(ChatMapper.class);


    @Mapping(source = "id", target = "roleId")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    Chat fromChatRole(ChatRole chatRole);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copyProperties(ChatUpdateRequest request, @MappingTarget Chat chat);
}
