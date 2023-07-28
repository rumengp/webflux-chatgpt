package com.anii.querydsl.gpt;

import com.anii.querydsl.enums.chat.MessageTypeEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message {

    private String role;

    private String content;

    private String name;

    @JsonProperty("function_all")
    private String functionAll;

    public static Message ofUserContent(String content) {
        return new Message(MessageTypeEnum.USER.getCode(), content, null, null);
    }

    public static Message ofSystemContent(String content) {
        return new Message(MessageTypeEnum.SYSTEM.getCode(), content, null, null);
    }
}
