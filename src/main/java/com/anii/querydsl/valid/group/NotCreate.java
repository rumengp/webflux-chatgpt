package com.anii.querydsl.valid.group;

import jakarta.validation.GroupSequence;

/**
 * 分组校验，非新建
 */
@GroupSequence({Delete.class, Query.class, Update.class})
public interface NotCreate {
}
