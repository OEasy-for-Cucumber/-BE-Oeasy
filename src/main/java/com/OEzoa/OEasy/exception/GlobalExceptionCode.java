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
    BAD_REQUEST(HttpStatus.BAD_REQUEST,"잘못된 요청입니다.", "개발자가 의도하지 않은 요청"),

    //---------------index--------------------
    INDEX_API_REQUEST_FAILURE(HttpStatus.SERVICE_UNAVAILABLE, "[일시적 오류] 잠시 후 다시 시도해 주세요.", "API 호출 실패"),

    //---------------tip----------------------

    //---------------community----------------
    COMMUNITY_NOT_FIND(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다", "잘못 들어온 pk"),
    COMMUNITY_NOT_SAME(HttpStatus.BAD_REQUEST, "본인 게시글만 수정, 삭제가 가능합니다.", "게시글의 요청자와 소유자가 다름"),
    COMMUNITY_NOT_FOUND_BOARD(HttpStatus.NOT_FOUND, "권한이 없거나 게시글을 찾을 수 없습니다.", "본인 게시물이 아니거라 board 조회를 실패"),
    COMMUNITY_COMMENT_NOT_FIND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다.", "잘못 들어온 pk"),

    //---------------member---------------------
    NOT_FIND_MEMBER(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다", "잘못 들어온 pk"),
    //---------------token validation-------------------
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 액세스 토큰입니다.", "액세스 토큰이 잘못되었거나 만료됨"),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "액세스 토큰이 만료되었습니다.", "JWT 액세스 토큰 만료"),
    UNSUPPORTED_TOKEN(HttpStatus.BAD_REQUEST, "지원하지 않는 토큰입니다.", "지원하지 않는 JWT 토큰 형식"),
    MALFORMED_TOKEN(HttpStatus.BAD_REQUEST, "잘못된 토큰 형식입니다.", "JWT 토큰 형식 오류"),

    //---------------sign up-------------------
    EMAIL_DUPLICATION(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다.", "중복된 이메일 요청"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호는 필수 항목입니다.", "비밀번호 입력 누락"),
    INVALID_VALID_SENTENCE(HttpStatus.BAD_REQUEST, "탈퇴 문구를 정확히 입력하세요.", "올바르지 않은 탈퇴 문구"),
    //---------------nickname-------------------
    NICKNAME_EMPTY(HttpStatus.BAD_REQUEST, "닉네임을 입력해주세요.", "닉네임이 null이거나 빈 문자열"),
    NICKNAME_TOO_LONG(HttpStatus.BAD_REQUEST, "닉네임은 최대 8글자까지 가능합니다.", "닉네임의 길이가 허용된 범위를 초과"),
    NICKNAME_DUPLICATION(HttpStatus.CONFLICT, "이미 사용 중인 닉네임입니다.", "이미 있는 닉네임으로 변경 요청"),
    INVALID_OLD_PASSWORD(HttpStatus.BAD_REQUEST, "기존 비밀번호가 일치하지 않습니다.", "사용자가 입력한 기존 비밀번호가 틀림"),
    //---------------AI OE-------------------
    ANSWER_GENERATION_FAILED(HttpStatus.BAD_REQUEST, "답변 생성에 실패했습니다오이!🥒.", "질문이 비어 있습니다."),
    MEMBER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "유효하지 않은 사용자입니다.", "TokenValidator에서 회원을 찾을 수 없습니다."),
    MEMBER_ALREADY_CONNECTED(HttpStatus.UNAUTHORIZED, "이미 연결되어 있습니다.", "이미 챗봇과 연결된 MemberPk입니다. "),
    QUESTION_TOO_LONG(HttpStatus.BAD_REQUEST,"질문이 너무 길어오이ㅠ 조금 더 간단히 해주세오이!🥒", "100자 이내로 질문해주세요"),
    INVALID_INPUT(HttpStatus.BAD_REQUEST,"죄송하지만 오이와 관련된 질문만 답변할 수 있어오이!🥒","오이를 포함해 질문해 주세요"),
    DB_SAVE_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "데이터 저장 중 오류가 발생했습니다.", "Hibernate insert 쿼리가 실패했습니다."),
    //---------------auth service-------------------
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다.", "리프레시 토큰이 만료되었거나 잘못된 형식입니다."),
    TOKEN_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원 정보를 찾을 수 없습니다.", "리프레시 토큰에서 추출한 사용자 ID가 존재하지 않음"),
    //---------------recipe-------------------
    RECIPE_OUT_OF_VALID_RANGE(HttpStatus.BAD_REQUEST, "범위를 벗어났습니다", "범위를 벗어난 데이터를 요청"),
    RECIPE_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "id를 확인해 주세요", "요청한 id에 해당하는 값이 없습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
    private final String description;

}
