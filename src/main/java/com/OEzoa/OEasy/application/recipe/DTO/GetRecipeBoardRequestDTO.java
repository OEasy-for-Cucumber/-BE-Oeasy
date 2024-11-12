package com.OEzoa.OEasy.application.recipe.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetRecipeBoardRequestDTO {
    @Schema(description = "DB에서 참조할 ID입니다. 최초 호출 시 값은 99999999를 사용해 주세요.")
    private long refId;
    @Schema(description = "한 번에 가져올 레시피 개수입니다.")
    private int view;
}
