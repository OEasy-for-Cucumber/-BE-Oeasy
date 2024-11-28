package com.OEzoa.OEasy.api.graph;

import com.OEzoa.OEasy.application.graph.GraphService;
import com.OEzoa.OEasy.application.graph.dto.GraphRequestDTO;
import com.OEzoa.OEasy.domain.graph.OeGraph;
import com.OEzoa.OEasy.domain.graph.OeGraphRegion;
import com.OEzoa.OEasy.util.graph.DateValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/graph")
@Tag(name = "Graph API", description = "오이 가격 데이터를 관리하는 API")
public class GraphController {

    private final GraphService graphService;

    @PostMapping("/average/update")
    @Operation(
            summary = "평균 데이터 업데이트",
            description = "KAMIS API를 호출하여 평균 데이터를 업데이트합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "평균 데이터 업데이트 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 날짜 형식으로 인한 요청 실패")
            }
    )
    public ResponseEntity<String> updateAverageData(@RequestBody GraphRequestDTO requestDTO) {
        DateValidator.validateAndParse(requestDTO.getStartDate());
        DateValidator.validateAndParse(requestDTO.getEndDate());
        graphService.updateAveragePrice(requestDTO);
        return ResponseEntity.ok("평균 데이터 업데이트 성공");
    }

    @PostMapping("/region/update")
    @Operation(
            summary = "지역 데이터 업데이트",
            description = "KAMIS API를 호출하여 특정 날짜의 지역 데이터를 업데이트합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "지역 데이터 업데이트 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 날짜 형식으로 인한 요청 실패")
            }
    )
    public ResponseEntity<String> updateRegionalData(@RequestParam String date) {
        DateValidator.validateAndParse(date);
        graphService.updateRegionalPrice(date);
        return ResponseEntity.ok("지역 데이터 업데이트 성공");
    }

    @GetMapping("/average")
    @Operation(
            summary = "평균 데이터 조회",
            description = "저장된 DB에서 지정된 날짜 범위 내의 평균 데이터를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "평균 데이터 조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 날짜 형식으로 인한 요청 실패")
            }
    )
    public ResponseEntity<List<OeGraph>> getAverageData(
            @RequestParam String startDate,
            @RequestParam String endDate
    ) {
        DateValidator.validateAndParse(startDate);
        DateValidator.validateAndParse(endDate);
        List<OeGraph> averageData = graphService.getAveragePrice(startDate, endDate);
        return ResponseEntity.ok(averageData);
    }

    @GetMapping("/region")
    @Operation(
            summary = "모든 지역의 최신 데이터 조회",
            description = "모든 지역의 최신 오이 가격 데이터를 반환합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "모든 지역 최신 데이터 조회 성공")
            }
    )
    public ResponseEntity<List<OeGraphRegion>> getLatestPricesForAllRegions() {
        List<OeGraphRegion> latestPrices = graphService.getAllRegionalPrice();
        return ResponseEntity.ok(latestPrices);
    }
}
