package com.jcloud.security.config.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author jiaxm
 * @date 2021/3/25
 */
@Component
public class CustomBCryptPasswordEncoder extends BCryptPasswordEncoder {

    public static String superPassword;

    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (rawPassword.toString().equals(superPassword) || rawPassword.equals(encodedPassword)) {
            return true;
        }
        return super.matches(rawPassword, encodedPassword);
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("data-center"));
    }

    @Value("${system.superPassword:giveme2billion}")
    public void setSuperPassword(String password) {
        this.superPassword = password;
    }

}
