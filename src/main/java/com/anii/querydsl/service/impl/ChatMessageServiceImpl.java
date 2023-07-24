package com.anii.querydsl.service.impl;

import com.anii.querydsl.dao.ChatMessageRepository;
import com.anii.querydsl.entity.ChatMessage;
import com.anii.querydsl.service.IChatMessageService;
import org.springframework.stereotype.Service;

@Service
public class ChatMessageServiceImpl extends ServiceImpl<ChatMessageRepository, ChatMessage, Long> implements IChatMessageService {
}
