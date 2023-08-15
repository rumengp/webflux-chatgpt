package com.anii.querydsl.request.chat.image;

public record ChatImageRolloutDTO(
        // 上一个图片ID
        Long preImageId,
        // 查询的数量
        Integer num) {

}
