package com.OEzoa.OEasy.application.community.DTO.Cmn;

import com.OEzoa.OEasy.domain.community.OeBoard;
import com.OEzoa.OEasy.domain.community.OeBoardImg;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder(toBuilder = true)
@Getter
public class CmnDTOResponseDTO {
    private long id;
    private String title;
    private String nickname;
    private String profileImg;
    private LocalDateTime createdAt;
    private String content;
    private long likes;
    private long view;
    private List<String> imageUrlList;
    private boolean liked;

    public static CmnDTOResponseDTO of(OeBoard board, boolean liked){
        List<String> imageUrlList = new ArrayList<>();
        if(!board.getImages().isEmpty()){
            for (OeBoardImg image : board.getImages()) {
                imageUrlList.add(image.getS3ImgAddress());
            }
        }
        return CmnDTOResponseDTO.builder()
                .id(board.getBoardPk())
                .content(board.getContent())
                .profileImg(board.getMember().getMemberImage())
                .title(board.getTitle())
                .nickname(board.getMember().getNickname())
                .createdAt(board.getBoardTimestamp())
                .likes(board.getLikes().size())
                .view(board.getViewCnt())
                .imageUrlList(imageUrlList)
                .liked(liked)
                .build();

    }

}
