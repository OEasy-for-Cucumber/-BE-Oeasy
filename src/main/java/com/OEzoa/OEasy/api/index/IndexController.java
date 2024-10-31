package com.OEzoa.OEasy.api.index;

import com.OEzoa.OEasy.application.index.IndexService;
import com.OEzoa.OEasy.application.index.OEIndexDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/index")
@Tag(name="OE Index API", description = "날씨 데이터를 수치화하여 그에 맞는 이미지를 보여줍니다")
public class IndexController {

    @Autowired
    private IndexService indexService;

    @GetMapping()
    @Operation(summary = "오이 지수 가져오기",
            description = "가장 최근 날씨 데이터를 가져와 오이 지수화합니다",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공")
                    //,@ApiResponse(responseCode = "400", description = "알 수 없는 오류")
            })
    public ResponseEntity<OEIndexDTO> getOEIndex() {
        return ResponseEntity.ok(indexService.getOEIndex());
    }

}
