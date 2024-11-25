package com.OEzoa.OEasy.infra.api;

import com.OEzoa.OEasy.application.graph.dto.GraphRequestDTO;
import com.OEzoa.OEasy.application.graph.dto.GraphResponseDTO;
import com.OEzoa.OEasy.domain.graph.OeGraph;
import com.OEzoa.OEasy.exception.GlobalException;
import com.OEzoa.OEasy.exception.GlobalExceptionCode;
import com.OEzoa.OEasy.util.KamisMapper;
import com.OEzoa.OEasy.util.KamisMapper.ItemNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
@Slf4j
@Service
@RequiredArgsConstructor
public class KamisService {

    private final RestTemplate restTemplate;

    @Value("${kamis.api.id}")
    private String apiId;

    @Value("${kamis.api.key}")
    private String apiKey;

    private static final String KAMIS_API_URL = "https://www.kamis.or.kr/service/price/xml.do?action=periodProductList";
    public List<OeGraph> fetchCucumberPriceData(GraphRequestDTO requestDTO) {
        // URL 빌드
        String url = buildUrl(requestDTO);
        log.info("KAMIS OPEN API 호출 URL: {}", url);

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            log.error("KAMIS OPEN API 호출에 실패했습니다. HTTP 상태 코드: {}, 응답 본문: {}",
                    response.getStatusCode(), response.getBody());
            throw new GlobalException(GlobalExceptionCode.DB_LOAD_FAILURE);
        }
        // 데이터 파싱 및 변환
        List<OeGraph> graphData = parseApiResponse(response.getBody());
        log.info("KAMIS OPEN API 데이터 파싱 완료. 데이터 개수: {}", graphData.size());
        return parseApiResponse(response.getBody());
    }

    private String buildUrl(GraphRequestDTO requestDTO) {
        log.info("KAMIS 요청 - 시작 날짜: {}, 종료 날짜: {}", requestDTO.getStartDate(), requestDTO.getEndDate());

        return UriComponentsBuilder.fromHttpUrl(KAMIS_API_URL)
                .queryParam("p_productclscode", "01") // 01 소매
                .queryParam("p_startday", requestDTO.getStartDate())
                .queryParam("p_endday", requestDTO.getEndDate())
                .queryParam("p_itemcategorycode", "200") // 부류 코드 200 채소
                .queryParam("p_itemcode", "223") // 품목코드 223 오이
                .queryParam("p_kindcode", "01") // 품종 코드 01 다다기 오이
                .queryParam("p_productrankcode", "04") // 등급
                .queryParam("p_countrycode", "1101") // 서울
                .queryParam("p_convert_kg_yn", "N")
                .queryParam("p_cert_key", apiKey)
                .queryParam("p_cert_id", apiId)
                .queryParam("p_returntype", "xml")
                .toUriString();
    }

    private List<OeGraph> parseApiResponse(String responseBody) {
        XmlMapper xmlMapper = new XmlMapper();
        List<OeGraph> graphData = new ArrayList<>();

        try {
            // XML 데이터를 KamisMapper 객체로 변환
            KamisMapper response = xmlMapper.readValue(responseBody, KamisMapper.class);

            if (!"000".equals(response.getData().getErrorCode())) {
                log.error("KAMIS API 응답 오류. 에러 코드: {}", response.getData().getErrorCode());
                throw new GlobalException(GlobalExceptionCode.DB_LOAD_FAILURE);
            }

            // 데이터 파싱 및 변환
            for (KamisMapper.ItemNode item : response.getData().getItems()) {
                // countyname 평균 만 받게
                String countyname = item.getCountyname();
                if (!"평균".equals(countyname)) {
                    continue;
                }

                String date = item.getYyyy() + "-" + item.getRegday().replace("/", "-");
                String priceStr = item.getPrice().replace(",", "");

                if (!"-".equals(priceStr)) {
                    Long price = Long.parseLong(priceStr) / 10;
                    graphData.add(new OeGraph(date, price));
                }
            }

            log.info("XML 데이터 파싱 성공. 총 {}개의 데이터 변환됨.", graphData.size());
        } catch (Exception e) {
            log.error("XML 데이터 파싱 중 오류 발생: {}", e.getMessage(), e);
            throw new GlobalException(GlobalExceptionCode.DB_LOAD_FAILURE);
        }

        return graphData;
    }

}
