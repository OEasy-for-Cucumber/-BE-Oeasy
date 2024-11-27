package com.OEzoa.OEasy.application.community.DTO.Cmn;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CmnCreateRequestDTO {

    private long userId;
    private String title;
    private String content;
    private List<MultipartFile> imgList;



}
