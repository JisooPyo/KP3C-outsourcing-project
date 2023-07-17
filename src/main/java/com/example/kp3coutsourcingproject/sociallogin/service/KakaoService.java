package com.example.kp3coutsourcingproject.sociallogin.service;

import com.example.kp3coutsourcingproject.jwt.JwtUtil;
import com.example.kp3coutsourcingproject.sociallogin.dto.KakaoUserInfoDto;
import com.example.kp3coutsourcingproject.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;

@Slf4j(topic = "KAKAO Login")
@Service
@RequiredArgsConstructor
public class KakaoService {

    // 주입받을 것들
//    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;

    public String kakaoLogin(String code) throws JsonProcessingException {
        //1. 인가 코드로 액세스 토큰 요청
        String accessToken = getToken(code);
        //2. 토큰으로 카카오api호출 : "액세스토큰"으로 "카카오 사용자 정보"가저오기
        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);
        return null;
    }

    public String getToken(String code) throws JsonProcessingException {
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("http://kauth.kakao.com")
                .path("oauth/token")
                .encode()
                .build()
                .toUri();

        //HTTP header 생성

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencode;charset=utf-8");

        //HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "본인의 REST API키"); //3f5211af4262100b1a6a78532a532b01 입력
        body.add("redirect_uri", "http://localhost:8080/kp3c/user/kakao/callback");
        body.add("code", code);

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity.post(uri)
                .headers(headers)
                .body(body);

        // HTTP 요청 보내기

        ResponseEntity<String> response = restTemplate.exchange(
                requestEntity, String.class);

        // HTTP 응답 (Json) -> 액세스 토큰 파싱

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        return jsonNode.get("access_token").asText();
    }

    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException{
        //요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("http://kapi.kakao.com")
                .path("/v2/user/me")
                .encode()
                .build()
                .toUri();


    }
}
