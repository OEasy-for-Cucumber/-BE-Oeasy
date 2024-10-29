//package com.OEzoa.OEasy.infra.api;
//
//import net.minidev.json.JSONObject;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.http.*;
//
//@Service
//public class KakaoAPIService {
//
//    private static final String KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
//    private static final String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";
//    private static final String CLIENT_ID = ""; // 카카오 앱 REST API 키
//    private static final String REDIRECT_URI = ""; // 카카오 앱의 리다이렉트 URI
//
//    /**
//     * 카카오 인가 코드를 이용하여 액세스 토큰을 발급받습니다.
//     * @param code 카카오 인가 코드
//     * @return 액세스 토큰
//     */
//    public String getKakaoAccessToken(String code) {
//        RestTemplate restTemplate = new RestTemplate();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        String body = "grant_type=authorization_code" +
//                "&client_id=" + CLIENT_ID +
//                "&redirect_uri=" + REDIRECT_URI +
//                "&code=" + code;
//
//        HttpEntity<String> request = new HttpEntity<>(body, headers);
//        ResponseEntity<String> response = restTemplate.postForEntity(KAKAO_TOKEN_URL, request, String.class);
//
//        if (response.getStatusCode() == HttpStatus.OK) {
//            JSONObject json = new JSONObject(response.getBody());
//            return json.getString("access_token");
//        }
//
//        return null;
//    }
//
//    /**
//     * 액세스 토큰을 이용하여 카카오 사용자 정보를 가져옵니다.
//     * @param accessToken 카카오 액세스 토큰
//     * @return 사용자 정보 JSONObject
//     */
//    public JSONObject getUserInfo(String accessToken) {
//        RestTemplate restTemplate = new RestTemplate();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(accessToken);
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//
//        ResponseEntity<String> response = restTemplate.exchange(KAKAO_USER_INFO_URL, HttpMethod.GET, entity, String.class);
//
//        if (response.getStatusCode() == HttpStatus.OK) {
//            return new JSONObject(response.getBody());
//        }
//
//        return null;
//    }
//}
