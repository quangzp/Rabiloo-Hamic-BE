package com.project.config;

import com.project.entity.UserEntity;
import com.project.security.CustomUserDetail;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserAuth {

    public UserEntity getCurrent() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        return ((CustomUserDetail) authentication.getPrincipal()).getUserEntity();
    }
}
