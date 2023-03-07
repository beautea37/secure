package com.fullstack.secure;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class PasswordEncoderTests {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testEncryptTests(){
        String password = "123";

        /*
        BCryptPasswordEncoder 특징

        1.복호화 안됨
        2. 일정 길이의 암호화된 문자열 리턴
        3. 복호화는 안되지만 Raw password를 Encrypt된 값과 같은지 비교하는 matches()메서드 제공.
         */

        String encryptPw = passwordEncoder.encode(password);
        System.out.println(password + " : " + encryptPw);
        System.out.println(passwordEncoder.matches(password, encryptPw));
    }
}
