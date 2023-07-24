package com.anii.querydsl.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("chat")
public class Chat implements Serializable {

    /**
     * id
     */
    @Id
    private Long id;

    /**
     * 所属用户id
     */
    @CreatedBy
    private String username;

    /**
     * 聊天所属角色
     */
    private Long roleId;

    /**
     * 聊天标题
     */
    private String title;

    /**
     * 角色指令
     */
    private String command;

    /**
     * 使用哪个模型
     */
    private String model;

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
