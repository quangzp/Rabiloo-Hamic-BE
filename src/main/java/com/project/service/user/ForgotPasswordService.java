package com.project.service.user;

import com.project.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ForgotPasswordService {
    @Autowired
    private UserRepository userRepository;

}
