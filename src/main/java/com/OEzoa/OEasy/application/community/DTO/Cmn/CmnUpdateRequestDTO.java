package com.OEzoa.OEasy.application.community.DTO.Cmn;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder(toBuilder = true)
@Setter
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
