package com.anii.querydsl.mapper;

import com.anii.querydsl.entity.Chat;
import com.anii.querydsl.entity.ChatMessage;
import com.anii.querydsl.gpt.chat.Completion;
import com.anii.querydsl.gpt.chat.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MessageMapper {

    MessageMapper MAPPER = Mappers.getMapper(MessageMapper.class);

    @Mapping(target = "role", expression = "java(message.getType().getCode())")
    Message toMessage(ChatMessage message);

    @Mapping(target = "model", expression = "java(chat.getModel().getCode())")
    @Mapping(target = "maxTokens", source = "chat.maxReplay")
    @Mapping(target = "presencePenalty", source = "chat.propertyTopic")
    @Mapping(target = "frequencyPenalty", source = "chat.propertyRepeat")
    @Mapping(target = "user", source = "chat.username")
    @Mapping(target = "topP", source = "chat.propertyRandom")
    Completion toCompletion(Chat chat, List<Message> messages);

}
