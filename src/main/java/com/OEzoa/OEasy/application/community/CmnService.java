package com.OEzoa.OEasy.application.community;

import com.OEzoa.OEasy.domain.community.BoardImgRepository;
import com.OEzoa.OEasy.domain.community.BoardRepository;
import com.OEzoa.OEasy.domain.community.OeBoard;
import com.OEzoa.OEasy.domain.community.OeBoardImg;
import com.OEzoa.OEasy.domain.member.Member;
import com.OEzoa.OEasy.util.s3Bucket.FileUploader;
import com.OEzoa.OEasy.util.timeTrace.TimeTrace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@TimeTrace
public class CmnService {
    
    private final BoardRepository boardRepository;
    private final BoardImgRepository boardImgRepository; 
    private final FileUploader fileUploader;

    /**
     * 게시글 작성
     * @param cmn
     * @param member
     */
    public void createCmn(CmnCreateRequestDTO cmn, Member member){
        OeBoard board = OeBoard.of(cmn, member);
        board = boardRepository.save(board);
        
        if(!cmn.getImgList().isEmpty()){
            List<OeBoardImg> urlList = new ArrayList<>();
            for (MultipartFile multipartFile : cmn.getImgList()) {
                String uniqueImageKey = member.getNickname() + "_" + UUID.randomUUID();
                urlList.add(OeBoardImg.of(board, fileUploader.uploadFile(multipartFile, uniqueImageKey)));
            }
            boardImgRepository.saveAll(urlList);
        }
    }

    public CmnDTOResponse getCmn(OeBoard board){
        return CmnDTOResponse.of(board);
    }

    public void deleteCmn(OeBoard board){
        boardRepository.delete(board);
    }

}
