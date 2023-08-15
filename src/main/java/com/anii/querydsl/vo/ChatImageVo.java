package com.anii.querydsl.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatImageVo implements Serializable {

    private Long id;

    private ChatImageVoTypeEnum type;

    private String content;

    public enum ChatImageVoTypeEnum {
        PROMPT, USER_IMAGE, RESP_IMAGE;
    }
}
