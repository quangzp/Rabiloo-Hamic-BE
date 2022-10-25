package com.project.service.user;

import com.project.entity.RegisterUser;
import com.project.repository.RegisterUserRepository;
import com.project.repository.UserRepository;
import com.project.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
public class RegisterUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RegisterUserRepository repository;

    @Value("${fe-link}")
    private String feLink;

    private String genRandomStringCode() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }

    private String genRegisterMailContent(String registerCode, String email) {
        return "<p>Bạn đang đăng ký tài khoản tại webcontest-hamic-team7.</p>" +
                "<p>Hãy nhấp vào <a href=\"" +
                feLink + "/sign-up?email=" + email + "&registerCode=" + registerCode +
                "\">Link đăng ký!</a> này để tiếp tục</p>\n" +
                "<p>Nếu không phải bạn xin vui lòng bỏ qua email này</p>";
    }

    public boolean checkRegisterCode(String email, String code) {
        return repository.findByEmailAndRegisterCode(email, code).isPresent();
    }

    public String requestRegister(String email) {
        //TODO add validate email later
        String code = genRandomStringCode();
        new Thread(() -> emailService.senMimeMessageMail(
                email,
                "HAMIC WEBCONTEST - CONFIRM REGISTER",
                genRegisterMailContent(code, email)
        )).start();

        RegisterUser registerUser = new RegisterUser();
        registerUser.setEmail(email);
        registerUser.setRegisterCode(code);
        registerUser.setRequestTime(new Date());
        repository.save(registerUser);

        return "request register success";
    }
}
