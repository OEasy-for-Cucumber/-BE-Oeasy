package com.OEzoa.OEasy.application.community;

import com.OEzoa.OEasy.domain.community.OeBoard;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder(toBuilder = true)
@Getter
public class CmnDTOResponse {
    private long id;
    private String title;
    private String nickname;
    private LocalDateTime createdAt;
    private long likes;
    private List<String> imageUrlList;

    public static CmnDTOResponse of(OeBoard board){
        List<String> imageUrlList = new ArrayList<>();
        if(!board.getImages().isEmpty()){
            for (String s : imageUrlList) {
                imageUrlList.add(s);
            }
        }
        return CmnDTOResponse.builder()
                .id(board.getBoardPk())
                .title(board.getTitle())
                .nickname(board.getMember().getNickname())
                .createdAt(board.getBoardTimestamp())
                .likes(board.getLikes().size())
                .imageUrlList(imageUrlList)
                .build();

    }

}
