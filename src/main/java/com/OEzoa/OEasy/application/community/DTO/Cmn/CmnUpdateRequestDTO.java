package com.OEzoa.OEasy.application.community.DTO.Cmn;

import jakarta.validation.constraints.Null;
import lombok.*;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CmnUpdateRequestDTO {
    private long userId;
    private long communityId;
    private String title;
    private String content;
    private List<String> deleteList;

    private List<MultipartFile> imgList;
}
