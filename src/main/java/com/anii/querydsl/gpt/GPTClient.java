package com.anii.querydsl.gpt;

import com.anii.querydsl.gpt.chat.Completion;
import com.anii.querydsl.gpt.image.ImageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GPTClient {

    /**
     * 流式聊天，返回的是一个chunk
     *
     * @param completion
     * @return
     */
    Flux<String> chatStream(Completion completion);

    /**
     * 返回完整的答案
     *
     * @param completion
     * @return
     */
    Mono<String> chat(Completion completion);

    /**
     * 请求b64_json格式的图片，需要解码后返回给用户
     *
     * @param request
     * @return 返回解码后的byte数组
     */
    Flux<byte[]> createImageB64Json(ImageRequest request);

    /**
     * 请求图片的url地址，通过url下载图片
     * @param request
     * @return 图片的URL地址
     */
    Flux<String> createImageUrl(ImageRequest request);
}
