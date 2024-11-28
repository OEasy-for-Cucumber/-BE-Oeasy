package com.OEzoa.OEasy.infra.api;

import com.OEzoa.OEasy.application.graph.dto.GraphRequestDTO;
import com.OEzoa.OEasy.domain.graph.GraphRepository;
import com.OEzoa.OEasy.domain.graph.OeGraph;
import com.OEzoa.OEasy.domain.graph.OeGraphRegion;
import com.OEzoa.OEasy.domain.graph.GraphRegionRepository;
import com.OEzoa.OEasy.exception.GlobalException;
import com.OEzoa.OEasy.exception.GlobalExceptionCode;
import com.OEzoa.OEasy.util.graph.KamisMapper;
import com.OEzoa.OEasy.util.graph.RegionMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private final GraphRegionRepository graphRegionRepository;
    private final GraphRepository graphRepository;

    @Value("${kamis.api.id}")
    private String apiId;

    @Value("${kamis.api.key}")
    private String apiKey;

    private static final String AVERAGE_URL = "https://www.kamis.or.kr/service/price/xml.do?action=periodProductList";
    private static final String REGION_URL = "https://www.kamis.or.kr/service/price/xml.do?action=ItemInfo";

    /**
     * 평균 데이터 가져오기 및 저장
     */
    public void fetchAndUpdateAveragePriceData(GraphRequestDTO requestDTO) {
        String startDate = requestDTO.getStartDate();
        String endDate = requestDTO.getEndDate();

        String url = buildAverageUrl(startDate, endDate);
        log.info("KAMIS 평균 데이터 URL: {}", url);

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            log.error("KAMIS 평균 데이터 API 호출 실패. HTTP 상태 코드: {}, 응답 본문: {}",
                    response.getStatusCode(), response.getBody());
            throw new GlobalException(GlobalExceptionCode.DB_LOAD_FAILURE);
        }

        List<OeGraph> averageData = parseAverageResponse(response.getBody());
        graphRepository.saveAll(averageData); // 평균 데이터 저장
        log.info("평균 데이터 저장 완료. 저장된 데이터 수: {}", averageData.size());
    }

    /**
     * 지역 데이터 가져오기 및 저장
     */
    public void fetchAndUpdateRegionalPriceData(String date) {
        String url = buildRegionalUrl(date);
        log.info("KAMIS 지역 데이터 URL: {}", url);

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            log.error("KAMIS 지역 데이터 API 호출 실패. HTTP 상태 코드: {}, 응답 본문: {}",
                    response.getStatusCode(), response.getBody());
            throw new GlobalException(GlobalExceptionCode.DB_LOAD_FAILURE);
        }

        List<OeGraphRegion> regionData = parseRegionalResponse(response.getBody());
        saveAndUpdateRegions(regionData);
    }


    /**
     * 평균 데이터 URL 생성
     */
    private String buildAverageUrl(String startDate, String endDate) {
        return UriComponentsBuilder.fromHttpUrl(AVERAGE_URL)
                .queryParam("p_productclscode", "01") // 01 소매
                .queryParam("p_startday", startDate)
                .queryParam("p_endday", endDate)
                .queryParam("p_itemcategorycode", "200") // 부류 코드 200 채소
                .queryParam("p_itemcode", "223") // 품목코드 223 오이
                .queryParam("p_kindcode", "01") // 품종 코드 01 다다기 오이
                .queryParam("p_productrankcode", "04") // 등급
//                .queryParam("p_countrycode", "1101") // 서울
                .queryParam("p_convert_kg_yn", "N")
                .queryParam("p_cert_key", apiKey)
                .queryParam("p_cert_id", apiId)
                .queryParam("p_returntype", "xml")
                .toUriString();
    }


    /**
     * 지역 데이터 URL 생성
     */
    private String buildRegionalUrl(String date) {
        log.info("KAMIS 지역 요청 - 날짜: {}", date);

        return UriComponentsBuilder.fromHttpUrl(REGION_URL)
                .queryParam("p_productclscode", "01") // 01 소매
                .queryParam("p_regday", date) // 특정 날짜
                .queryParam("p_itemcategorycode", "200") // 부류 코드 200 채소
                .queryParam("p_itemcode", "223") // 품목코드 223 오이
                .queryParam("p_kindcode", "01") // 품종 코드 01 다다기 오이
                .queryParam("p_productrankcode", "04") // 등급
                .queryParam("p_convert_kg_yn", "Y") // kg 단위 변환
                .queryParam("p_cert_key", apiKey)
                .queryParam("p_cert_id", apiId)
                .queryParam("p_returntype", "xml")
                .toUriString();
    }

    /**
     * 평균 데이터 파싱
     */
    private List<OeGraph> parseAverageResponse(String responseBody) {
        XmlMapper xmlMapper = new XmlMapper();
        List<OeGraph> graphData = new ArrayList<>();

        try {
            KamisMapper response = xmlMapper.readValue(responseBody, KamisMapper.class);

            if (!"000".equals(response.getData().getErrorCode())) {
                log.error("KAMIS 평균 데이터 API 응답 오류. 에러 코드: {}", response.getData().getErrorCode());
                throw new GlobalException(GlobalExceptionCode.DB_LOAD_FAILURE);
            }

            for (KamisMapper.ItemNode item : response.getData().getItems()) {
                String countyname = item.getCountyname();
                if (!"평균".equals(countyname)) {
                    continue;
                }
                String date = item.getYyyy() + "-" + item.getRegday().replace("/", "-");
                String priceStr = item.getPrice().replace(",", "");

                if (!"-".equals(priceStr)) {
                    Long price = Long.parseLong(priceStr) / 10; // 10개 기준 -> 1개 기준
                    graphData.add(new OeGraph(date, price, countyname));
                }
            }

            log.info("평균 데이터 파싱 완료. 총 {}개의 데이터 변환됨.", graphData.size());
        } catch (Exception e) {
            log.error("평균 데이터 파싱 중 오류 발생: {}", e.getMessage(), e);
            throw new GlobalException(GlobalExceptionCode.DB_LOAD_FAILURE);
        }

        return graphData;
    }

    /**
     * 지역 데이터 파싱 및 매핑
     */
    private List<OeGraphRegion> parseRegionalResponse(String responseBody) {
        XmlMapper xmlMapper = new XmlMapper();
        Map<String, List<Long>> regionPriceMap = new HashMap<>();

        try {
            KamisMapper response = xmlMapper.readValue(responseBody, KamisMapper.class);

            if (!"000".equals(response.getData().getErrorCode())) {
                log.error("KAMIS 지역 데이터 API 응답 오류. 에러 코드: {}", response.getData().getErrorCode());
                throw new GlobalException(GlobalExceptionCode.DB_LOAD_FAILURE);
            }

            for (KamisMapper.ItemNode item : response.getData().getItems()) {
                String rawRegion = item.getCountyname();
                String mappedRegion = RegionMapper.mapRegion(rawRegion);
                String priceStr = item.getPrice().replace(",", "");

                if (!"-".equals(priceStr)) {
                    Long price = Long.parseLong(priceStr) / 10; // 10개 기준 -> 1개 기준
                    regionPriceMap.computeIfAbsent(mappedRegion, k -> new ArrayList<>()).add(price);
                }
            }
        } catch (Exception e) {
            log.error("지역 데이터 파싱 중 오류 발생: {}", e.getMessage(), e);
            throw new GlobalException(GlobalExceptionCode.DB_LOAD_FAILURE);
        }

        // 평균 계산 후 OeGraphRegion 리스트 생성
        List<OeGraphRegion> regionData = new ArrayList<>();
        String today = LocalDate.now().toString();
        for (Map.Entry<String, List<Long>> entry : regionPriceMap.entrySet()) {
            String region = entry.getKey();
            List<Long> prices = entry.getValue();
            Long averagePrice = prices.stream().mapToLong(Long::longValue).sum() / prices.size();
            regionData.add(new OeGraphRegion(null, today, averagePrice, region));
        }

        log.info("지역 데이터 매핑 및 평균 계산 완료. 총 {}개의 지역 데이터 변환됨.", regionData.size());
        return regionData;
    }

    /**
     * 기존 데이터 삭제 후 지역 데이터 저장
     */
    private void saveAndUpdateRegions(List<OeGraphRegion> regionData) {
        graphRegionRepository.deleteAll(); // 기존 데이터 삭제
        graphRegionRepository.saveAll(regionData); // 새로운 데이터 저장
        log.info("지역 데이터 저장 완료. 저장된 데이터 수: {}", regionData.size());
    }
}
