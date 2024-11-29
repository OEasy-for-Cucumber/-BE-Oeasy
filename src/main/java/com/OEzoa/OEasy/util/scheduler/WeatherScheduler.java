package com.OEzoa.OEasy.util.scheduler;

import com.OEzoa.OEasy.domain.index.Weather;
import com.OEzoa.OEasy.domain.index.WeatherImg;
import com.OEzoa.OEasy.domain.index.WeatherImgRepository;
import com.OEzoa.OEasy.domain.index.WeatherRepository;
import com.OEzoa.OEasy.util.timeTrace.TimeTrace;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;

@Service
@TimeTrace
@RequiredArgsConstructor
public class WeatherScheduler {

    private final WeatherRepository weatherRepository;
    private final WeatherImgRepository weatherImgRepository;

    @Value("${weather.api.key}")
    private String weatherKey;

    @Scheduled(cron = "00 30 * * * *") // 매 시간의 30분 마다 작업 초/분/시/일/월/요일(0 to 7)
    //@Scheduled(fixedRate = 100000)
    public void fetchWeatherData() throws IOException {
        // 현재 시간을 기준으로 baseDate와 baseTime 설정
        LocalDateTime now = LocalDateTime.now();
        URL url = new URL("https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst"
                + "?serviceKey=" + weatherKey
                + "&pageNo=1"
                + "&numOfRows=100"
                + "&dataType=JSON"
                + "&base_date=" + now.getYear() + String.format("%02d",now.getMonthValue()) + String.format("%02d",now.getDayOfMonth())
                + "&base_time=" + String.format("%02d",now.getHour()) + 30
                + "&nx=59&ny=125");
        // URL 객체를 통해 HTTP 연결을 설정하고 HttpURLConnection 객체로 캐스팅
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // JSON 처리를 위한 Jackson의 ObjectMapper 객체 생성
        ObjectMapper objectMapper = new ObjectMapper();

        // API 응답을 JSON으로 읽어서 JsonNode 객체로 변환
        // readTree 메서드는 입력 스트림을 받아 JSON 구조로 파싱하여 JsonNode 트리로 변환
        JsonNode root = objectMapper.readTree(con.getInputStream());

        // JsonNode 트리를 탐색하여 원하는 데이터를 추출
        // "response" -> "body" -> "items" -> "item" 순으로 JSON 구조를 파고들어
        // 최종적으로 "item" 노드에 접근하여 필요한 데이터를 추출할 준비를 합니다
        JsonNode items = root.path("response").path("body").path("items").path("item");

        Weather weather;
        WeatherImg img = null;
        int weatherNum = 0;
        double temperature = 0;
        // JSON 배열인 items에서 category별로 값을 찾아 할당
        for (JsonNode item : items) {
            String category = item.path("category").asText();
            String value = item.path("obsrValue").asText(); // obsrValue로 값을 가져옴

            switch (category) {
                case "PTY":// 날씨
                    weatherNum = Integer.parseInt(value);
                    break;
                case "T1H":// 기온
                    temperature = Double.parseDouble(value);
                    break;
            }
        }
        int imgNum = 0;
        if(weatherNum !=0) {
            switch (weatherNum) {
                case 1:
                case 2:
                case 5:
                case 6:
                    imgNum = 1;//비
                    break;
                default:
                    imgNum=3; //눈
            }
        }else{
            if(temperature < 5) imgNum = 10; //추운 오이
            else if(20 <= temperature && temperature < 24) imgNum = 11; //기쁜오이
            else if(28 < temperature) imgNum = 12; //더운오이
            else imgNum = 13;//보통오이
        }

        weather = Weather.builder()
                .temperature(temperature)
                .weatherWebImg(weatherImgRepository.findById(imgNum).get())
                .weatherTimestamp(LocalDateTime.now())
                .state(parsePrecipitation(weatherNum))
                .build();
        weatherRepository.save(weather);

    }

    private static String parsePrecipitation(int num) {
        switch (num) {
            case 0:
                return "맑음";
            case 1:
                return "비";
            case 2:
                return "비/눈";
            case 3:
                return "눈";
            case 4:
                return "빗방울";
            case 6:
                return "빗방울/눈날림";
            case 7:
                return "눈날림";
            default:
                return "알 수 없음";
        }
    }
}
