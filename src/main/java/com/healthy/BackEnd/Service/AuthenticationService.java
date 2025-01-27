// AuthenticationService.java
package com.healthy.BackEnd.Service;

import com.healthy.BackEnd.dto.LoginDTO;
import com.healthy.BackEnd.entity.Users;
import com.healthy.BackEnd.repository.AuthenticationRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.util.regex.Pattern.matches;

@Service
public class AuthenticationService {
    @Autowired
    private AuthenticationRepository authenticationRepository;

    @NotNull
    public Users register(Users users) {
        // Mã hóa mật khẩu trước khi lưu
        return authenticationRepository.save(users);
    }

    @NotNull
    public boolean login(LoginDTO loginDTO) {
        // Tìm người dùng theo tên đăng nhập
        Users user = authenticationRepository.findByUsername(loginDTO.getUsername());
        // Kiểm tra mật khẩu
        return user != null && matches(loginDTO.getPassword(), user.getPasswordHash());
    }
}