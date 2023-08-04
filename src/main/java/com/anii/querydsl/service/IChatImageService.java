package com.anii.querydsl.service;

import com.anii.querydsl.request.chat.image.ChatImageCreateRequest;
import reactor.core.publisher.Flux;

public interface IChatImageService {

    /**
     * 返回的是图片的b64字符串，前端需要解码成图片显示
     */
    Flux<String> createImage(ChatImageCreateRequest request, String username);
}
