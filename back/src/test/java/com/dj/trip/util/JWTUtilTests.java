package com.dj.trip.util;

import com.dj.trip.global.util.JWTUtil;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
@Log4j2
public class JWTUtilTests {

    @Autowired
    private JWTUtil jwtUtil;

    @Test
    public void testGenerate() {
        Map<String, Object> claimMap = Map.of("mid", "ABCDE");
        String jwtStr = jwtUtil.generateToken(claimMap, 1);
        log.info(jwtStr);
    }

    @Test
    public void testValidate() {
        String jwtStr = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3MTQzMDE1NzcsIm1pZCI6IkFCQ0RFIiwiaWF0IjoxNzE0MzAxNTE3fQ.hxPSKg2IENe6Q92h57Sr7y8WPLNk3ZbRfksL6F1A8IA";
        Map<String, Object> claim = jwtUtil.validateToken(jwtStr);
        log.info(claim);
    }
}
