package com.anii.querydsl.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@Table("chat_message")
public class ChatMessage implements Serializable {

    /**
     * id
     */
    @Id
    private Long id;

    /**
     * 所属用户id
     */
    private Long userId;

    /**
     * 聊天所属角色
     */
    private Long chatId;

    /**
     * 消息类型，USER,ASSISTANT
     */
    private String type;

    /**
     * 聊天内容
     */
    private String content;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
