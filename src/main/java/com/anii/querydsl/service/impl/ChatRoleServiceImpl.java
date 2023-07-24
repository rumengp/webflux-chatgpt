package com.anii.querydsl.service.impl;

import com.anii.querydsl.dao.ChatRoleRepository;
import com.anii.querydsl.entity.ChatRole;
import com.anii.querydsl.service.IChatRoleService;
import org.springframework.stereotype.Service;

@Service
public class ChatRoleServiceImpl extends ServiceImpl<ChatRoleRepository, ChatRole, Long> implements IChatRoleService {
}
