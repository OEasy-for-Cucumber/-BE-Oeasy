package com.OEzoa.OEasy.application.tip;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class OeTipDTO {

    @Schema(description = "내용", example = "참깨, 브로콜리, 양배추, 딸기, 살구 등에 들어있는 리그난(Lignan)은\n" +
            "대장암과 폐경 후 유방암을 예방하는 효능이 있습니다.")
    private String content;
    List<OeTipTitleDTO> oeTipTitleDTOList;
}
