package com.OEzoa.OEasy.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GlobalExceptionCode {
    //---------------global-------------------
    EXAMPLE_EXCEPTION(HttpStatus.BAD_REQUEST, "사용자에게 보여줄 메시지", "개발자가 볼 메시지"),
    DB_LOAD_FAILURE(HttpStatus.SERVICE_UNAVAILABLE, "[알 수 없는 오류] 잠시 후 다시 시도해 주세요.", "데이터 베이스에서 값을 가져오는 것을 실패"),

    //---------------index--------------------
    INDEX_API_REQUEST_FAILURE(HttpStatus.SERVICE_UNAVAILABLE, "[일시적 오류] 잠시 후 다시 시도해 주세요.", "API 호출 실패"),

    //---------------tip----------------------


    //---------------recipe-------------------
    RECIPE_OUT_OF_VALID_RANGE(HttpStatus.BAD_REQUEST, "범위를 벗어났습니다", "범위를 벗어난 데이터를 요청"),
    RECIPE_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "id를 확인해 주세요", "요청한 id에 해당하는 값이 없습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
    private final String description;



}
