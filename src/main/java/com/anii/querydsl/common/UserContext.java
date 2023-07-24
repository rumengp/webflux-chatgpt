package com.anii.querydsl.common;

import java.util.List;

public record UserContext(Long id, String username, List<String> roles) {
}
