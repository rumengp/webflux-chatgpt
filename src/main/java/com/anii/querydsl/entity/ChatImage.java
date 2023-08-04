package com.anii.querydsl.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.util.List;

@Table("chat_image")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatImage {

    @Id
    private Long id;

    @CreatedBy
    private String username;

    private String prompt;

    private String imageObject;

    private String maskObject;

    private List<String> respObject;

    private Property property;

    @Data
    public static class Property implements Serializable {

        private String prompt;

        private Integer n;

        private String size;

        private String responseFormat;

        private String user;
    }
}


