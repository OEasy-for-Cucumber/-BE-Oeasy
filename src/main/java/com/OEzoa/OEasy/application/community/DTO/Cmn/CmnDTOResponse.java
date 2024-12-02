package com.OEzoa.OEasy.application.community.DTO.Cmn;

import com.OEzoa.OEasy.domain.community.OeBoard;
import com.OEzoa.OEasy.domain.community.OeBoardImg;
import com.OEzoa.OEasy.domain.image.OeImage;
import lombok.Builder;
import lombok.Getter;

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
    private String content;
    private long likes;
    private List<String> imageUrlList;
    private boolean liked;

    public static CmnDTOResponse of(OeBoard board, boolean liked){
        List<String> imageUrlList = new ArrayList<>();
        if(!board.getImages().isEmpty()){
            for (OeBoardImg image : board.getImages()) {
                imageUrlList.add(image.getS3ImgAddress());
            }
        }
        return CmnDTOResponse.builder()
                .id(board.getBoardPk())
                .content(board.getContent())
                .title(board.getTitle())
                .nickname(board.getMember().getNickname())
                .createdAt(board.getBoardTimestamp())
                .likes(board.getLikes().size())
                .imageUrlList(imageUrlList)
                .liked(liked)
                .build();

    }

}
