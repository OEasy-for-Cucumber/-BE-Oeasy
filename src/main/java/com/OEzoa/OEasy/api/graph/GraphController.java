package com.OEzoa.OEasy.api.graph;

import com.OEzoa.OEasy.application.graph.GraphService;
import com.OEzoa.OEasy.application.graph.dto.GraphRequestDTO;
import com.OEzoa.OEasy.application.graph.dto.GraphResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/graph")
@RequiredArgsConstructor
@Tag(name = "Cucumber Price API", description = "오이 가격 데이터를 관리합니다.")
public class GraphController {

    private final GraphService graphService;

    @PostMapping("/update")
    @Operation(
            summary = "오이 가격 데이터 업데이트",
            description = "공공 API에서 오이 가격 데이터를 가져와 저장 후 반환🥒.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "데이터 업데이트 및 반환 성공."),
                    @ApiResponse(responseCode = "500", description = "데이터 업데이트 실패.")
            }
    )
    public ResponseEntity<List<GraphResponseDTO>> updateCucumberPrices(@RequestBody GraphRequestDTO requestDTO) {
        List<GraphResponseDTO> updatedData = graphService.updateCucumberPriceData(requestDTO);
        return ResponseEntity.ok(updatedData);
    }

    @GetMapping("/range")
    @Operation(
            summary = "저장된 오이 가격 데이터 조회",
            description = "DB에 저장된 특정 기간의 오이 가격 데이터를 반환합니다.🥒",
            responses = {
                    @ApiResponse(responseCode = "200", description = "데이터 조회 성공."),
                    @ApiResponse(responseCode = "404", description = "요청한 기간에 데이터가 없습니다.")
            }
    )
    public ResponseEntity<List<GraphResponseDTO>> getCucumberPriceData(
            @RequestParam String startDate,
            @RequestParam String endDate) {

        List<GraphResponseDTO> data = graphService.getCucumberPriceData(startDate, endDate);
        return ResponseEntity.ok(data);
    }
}
