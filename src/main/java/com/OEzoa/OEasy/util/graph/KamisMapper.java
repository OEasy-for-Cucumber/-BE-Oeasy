package com.OEzoa.OEasy.util.graph;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) // 매핑되지 않은 상위 필드 무시
public class KamisMapper {
    private DataNode data;

    @Data
    public static class DataNode {
        @JacksonXmlProperty(localName = "error_code")
        private String errorCode;

        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "item")
        private List<ItemNode> items;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true) // 매핑되지 않은 item의 필드 무시
    public static class ItemNode {
        @JacksonXmlProperty(localName = "countyname")
        private String countyname; // 지역명 (평균, 최고값, 최저값 등 포함)

        @JacksonXmlProperty(localName = "price")
        private String price; // 가격 (숫자 또는 "-")

        @JacksonXmlProperty(localName = "yyyy")
        private String yyyy; // 연도 (평균 데이터용)

        @JacksonXmlProperty(localName = "regday")
        private String regday; // 날짜 (평균 데이터용)
    }
}
