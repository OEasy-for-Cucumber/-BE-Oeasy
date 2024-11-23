package com.OEzoa.OEasy.util;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
        @JacksonXmlProperty(localName = "yyyy")
        private String yyyy;

        @JacksonXmlProperty(localName = "regday")
        private String regday;

        @JacksonXmlProperty(localName = "price")
        private String price;

        @JacksonXmlProperty(localName = "countyname")
        public String countyname;
    }
}
