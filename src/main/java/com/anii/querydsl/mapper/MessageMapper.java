package com.anii.querydsl.mapper;

import com.anii.querydsl.entity.Chat;
import com.anii.querydsl.entity.ChatMessage;
import com.anii.querydsl.gpt.Completion;
import com.anii.querydsl.gpt.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MessageMapper {

    MessageMapper MAPPER = Mappers.getMapper(MessageMapper.class);

    @Mappings({
            @Mapping(target = "role", expression = "java(message.getType().getCode())")
    })
    Message toMessage(ChatMessage message);

    @Mappings({
            @Mapping(target = "model", expression = "java(chat.getModel().getCode())"),
            @Mapping(target = "maxTokens", source = "chat.maxReplay"),
            @Mapping(target = "presencePenalty", source = "chat.presencePenalty"),
            @Mapping(target = "frequencyPenalty", source = "")
    })
    Completion toCompletion(Chat chat, List<Message> messages);

}
