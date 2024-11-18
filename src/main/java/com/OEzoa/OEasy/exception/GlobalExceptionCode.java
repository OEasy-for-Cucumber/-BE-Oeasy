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

    //---------------nickname-------------------
    NICKNAME_EMPTY(HttpStatus.BAD_REQUEST, "닉네임을 입력해주세요.", "닉네임이 null이거나 빈 문자열"),
    NICKNAME_TOO_LONG(HttpStatus.BAD_REQUEST, "닉네임은 최대 8글자까지 가능합니다.", "닉네임의 길이가 허용된 범위를 초과"),

    //---------------recipe-------------------
    RECIPE_OUT_OF_VALID_RANGE(HttpStatus.BAD_REQUEST, "범위를 벗어났습니다", "범위를 벗어난 데이터를 요청"),
    RECIPE_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "id를 확인해 주세요", "요청한 id에 해당하는 값이 없습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
    private final String description;



}
