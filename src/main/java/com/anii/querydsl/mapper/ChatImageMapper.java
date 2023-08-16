package com.anii.querydsl.mapper;

import com.anii.querydsl.entity.ChatImage;
import com.anii.querydsl.gpt.image.ImageRequest;
import com.anii.querydsl.request.chat.image.ChatImageCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ChatImageMapper {

    ChatImageMapper MAPPER = Mappers.getMapper(ChatImageMapper.class);

    ImageRequest toImageRequest(ChatImageCreateRequest request);

    @Mapping(source = "request.prompt", target = "prompt")
    @Mapping(source = "objectNames", target = "respObject")
    @Mapping(source = "request", target = "property")
    ChatImage toDo(ChatImageCreateRequest request, List<String> objectNames);
}
