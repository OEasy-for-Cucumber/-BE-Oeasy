package com.OEzoa.OEasy.api.tip;

import com.OEzoa.OEasy.application.tip.TipService;
import com.OEzoa.OEasy.domain.tip.OeTipImg;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tip")
@RequiredArgsConstructor
@Tag(name="OE Tip API", description = "오이 꿀팁을 랜덤으로 가져옵니다")
public class TipController {

    private final TipService tipService;

    @GetMapping
    public ResponseEntity<OeTipImg> getTip() {
        return ResponseEntity.ok(tipService.getTip());
    }

}
