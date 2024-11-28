package com.OEzoa.OEasy.util.graph;

import com.OEzoa.OEasy.exception.GlobalException;
import com.OEzoa.OEasy.exception.GlobalExceptionCode;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateValidator {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 날짜 형식을 검증하는 메서드
     *
     * @param dateStr 검증할 날짜 문자열
     * @return 검증된 LocalDate 객체
     * @throws GlobalException 날짜 형식이 올바르지 않을 경우 GlobalException 발생
     */
    public static LocalDate validateAndParse(String dateStr) {
        try {
            return LocalDate.parse(dateStr, FORMATTER);
        } catch (DateTimeParseException e) {
            throw new GlobalException(GlobalExceptionCode.DATE_FORMAT_INVALID);
        }
    }
}
