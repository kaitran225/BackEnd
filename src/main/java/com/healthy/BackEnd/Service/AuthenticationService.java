
// AuthenticationService.java
package com.healthy.BackEnd.Service;

import com.healthy.BackEnd.dto.LoginDTO;
import com.healthy.BackEnd.entity.Users;
import com.healthy.BackEnd.repository.AuthenticationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.util.regex.Pattern.matches;

@Service
public class AuthenticationService {
    @Autowired
    private AuthenticationRepository authenticationRepository;


    public Users register(Users users) {
        // Mã hóa mật khẩu trước khi lưu

        Users newAccount = authenticationRepository.save(users);
        return newAccount;
    }
    public boolean login(LoginDTO loginDTO) {
        // Tìm người dùng theo tên đăng nhập
        Users user = authenticationRepository.findByUsername(loginDTO.getUsername());

        // Nếu không tìm thấy hoặc mật khẩu không khớp, trả về false
        if (user == null || !matches(loginDTO.getPassword(), user.getPasswordHash())) {
            return false;
        }

        // Nếu hợp lệ, trả về true
        return true;
    }


}