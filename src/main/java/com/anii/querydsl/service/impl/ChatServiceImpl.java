package com.anii.querydsl.service.impl;

import com.anii.querydsl.dao.ChatRepository;
import com.anii.querydsl.entity.Chat;
import com.anii.querydsl.service.IChatService;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl extends ServiceImpl<ChatRepository, Chat, Long> implements IChatService {
}
