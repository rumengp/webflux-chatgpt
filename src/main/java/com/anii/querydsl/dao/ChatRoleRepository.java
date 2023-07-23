package com.anii.querydsl.dao;

import com.anii.querydsl.entity.ChatRole;
import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository;

public interface ChatRoleRepository extends QuerydslR2dbcRepository<ChatRole, Long> {
}
