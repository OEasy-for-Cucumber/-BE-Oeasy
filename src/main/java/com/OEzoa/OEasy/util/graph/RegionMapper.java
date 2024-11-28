package com.OEzoa.OEasy.util.graph;

import java.util.HashMap;
import java.util.Map;

public class RegionMapper {
    private static final Map<String, String> REGION_MAP = new HashMap<>();

    static {
        REGION_MAP.put("서울", "서울");
        REGION_MAP.put("부산", "부산");
        REGION_MAP.put("대구", "대구");
        REGION_MAP.put("인천", "인천");
        REGION_MAP.put("광주", "광주");
        REGION_MAP.put("대전", "대전");
        REGION_MAP.put("울산", "울산");
        REGION_MAP.put("수원", "경기");
        REGION_MAP.put("성남", "경기");
        REGION_MAP.put("고양", "경기");
        REGION_MAP.put("용인", "경기");
        REGION_MAP.put("의정부", "경기");
        REGION_MAP.put("춘천", "강원");
        REGION_MAP.put("강릉", "강원");
        REGION_MAP.put("천안", "충남");
        REGION_MAP.put("청주", "충북");
        REGION_MAP.put("순천", "전남");
        REGION_MAP.put("전주", "전북");
        REGION_MAP.put("창원", "경남");
        REGION_MAP.put("김해", "경남");
        REGION_MAP.put("포항", "경북");
        REGION_MAP.put("안동", "경북");
        REGION_MAP.put("제주", "제주");
        REGION_MAP.put("세종", "세종");
    }

    public static String mapRegion(String rawRegion) {
        return REGION_MAP.getOrDefault(rawRegion, "알 수 없는 지역");
    }
}
