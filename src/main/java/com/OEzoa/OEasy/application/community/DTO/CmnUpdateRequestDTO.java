package com.OEzoa.OEasy.application.community.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CmnUpdateRequestDTO {
    private long userId;
    private long communityId;
    private String title;
    private String content;
    private List<MultipartFile> imgList;
}
