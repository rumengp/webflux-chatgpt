package com.anii.querydsl.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("chat_role")
public class ChatRole implements Serializable {

    /**
     * id
     */
    @Id
    private Long id;

    /**
     * 角色类型，SYSTEM，USER
     */
    private String type;

    /**
     * 角色所属的用户id
     */
    private String username;

    /**
     * 角色指令
     */
    private String command;

    /**
     * 使用哪个模型
     */
    private String model;

    /**
     * 角色昵称
     */
    private String nickName;

    /**
     * 备注
     */
    private String mark;

    /**
     * 打招呼用语
     */
    private String welcome;

    /**
     * 头像链接
     */
    private String picture;

    /**
     * 聊天上下文数量
     */
    private Integer contextNum;

    /**
     * 限制最大回复数量
     */
    private Integer maxReplay;

    /**
     * 随机属性0-1
     */
    private Double propertyRandom;

    /**
     * 词汇属性0-1
     */
    private Double propertyWord;

    /**
     * 话题属性(-2,2)
     */
    private Integer propertyTopic;

    /**
     * 重复属性(-2,2)
     */
    private Integer propertyRepeat;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
