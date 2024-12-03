package com.OEzoa.OEasy.application.community;




import com.OEzoa.OEasy.application.community.DTO.Cmn.*;
import com.OEzoa.OEasy.domain.community.*;
import com.OEzoa.OEasy.domain.member.Member;
import com.OEzoa.OEasy.domain.member.MemberRepository;
import com.OEzoa.OEasy.exception.GlobalException;
import com.OEzoa.OEasy.exception.GlobalExceptionCode;
import com.OEzoa.OEasy.util.s3Bucket.FileUploader;
import com.OEzoa.OEasy.util.timeTrace.TimeTrace;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@TimeTrace
public class CmnService {
    
    private final BoardRepository boardRepository;
    private final BoardImgRepository boardImgRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final MemberRepository memberRepository;
    private final FileUploader fileUploader;

    /**
     * 게시글 작성
     * @param cmn
     * @param member
     */
    public void createCmn(CmnCreateRequestDTO cmn, Member member){
        OeBoard board = OeBoard.of(cmn, member);
        board = boardRepository.save(board);
        
        if(cmn.getImgList() != null){
            List<OeBoardImg> urlList = new ArrayList<>();
            for (MultipartFile multipartFile : cmn.getImgList()) {
                if (multipartFile == null) continue;
                String uniqueImageKey = member.getNickname() + "_" + UUID.randomUUID();
                urlList.add(OeBoardImg.of(board, fileUploader.uploadFile(multipartFile, uniqueImageKey)));
            }
            boardImgRepository.saveAll(urlList);
        }
    }

    public CmnDTOResponseDTO getCmn(OeBoard board, Member member){
        Optional<OeBoardLike> oeBoardLike = boardLikeRepository.findByBoardAndMember(board, member);
        if(oeBoardLike.isPresent()){
            return CmnDTOResponseDTO.of(board, true);
        }else return CmnDTOResponseDTO.of(board, false);
    }

    // s3버킷 리팩토링할 것
    public void updateCmn(OeBoard board, CmnUpdateRequestDTO dto){
        if(dto.getDeleteList() != null) {
            for (String url : dto.getDeleteList()) {
                String key = fileUploader.extractKeyFromUrl(url);
                fileUploader.deleteImage(key);
                boardImgRepository.deleteByS3ImgAddress(url);
            }
        }
        board.of(dto.getTitle(), dto.getContent());
    
        //----이미지
        if(dto.getImgList() != null) {
            for (MultipartFile multipartFile : dto.getImgList()) {
                if(multipartFile == null) continue;
                String uniqueImageKey = board.getMember().getNickname() + "_" + UUID.randomUUID();
                OeBoardImg oeBoardImg = OeBoardImg.builder()
                        .board(board)
                        .s3ImgAddress(fileUploader.uploadFile(multipartFile, uniqueImageKey))
                        .build();
                boardImgRepository.save(oeBoardImg);
            }
        }

    }

    public void deleteCmn(OeBoard board){
        boardRepository.delete(board);
    }

    public void testInit(){
        List<Member> memberList = memberRepository.findAll();
        OeBoard board = boardRepository.findById(1L).get();
        for(Member member : memberList){
            boardLikeRepository.save(OeBoardLike.builder()
                            .member(member)
                            .board(board)
                    .build());
        }
    }

    public CmnBoardListResponseDTO searchBoard(CmnBoardListRequestDTO dto){
        Pageable pageable;
        if(dto.isSortType()) {
            pageable = PageRequest.of(dto.getPage(), dto.getSize(), Sort.by(Sort.Direction.ASC, dto.getSortKeyword()));
        }else{
            pageable = PageRequest.of(dto.getPage(), dto.getSize(), Sort.by(Sort.Direction.DESC, dto.getSortKeyword()));
        }

        return switch (dto.getSearchType()) {
            case "titleAndContent" -> CmnBoardListResponseDTO.of(boardRepository.findByTitleOrContent(dto.getSearchKeyword().trim(), pageable));
            case "title" -> CmnBoardListResponseDTO.of(boardRepository.findByTitle(dto.getSearchKeyword().trim(), pageable));
            case "nickname" -> CmnBoardListResponseDTO.of(boardRepository.findByNickname(dto.getSearchKeyword().trim(), pageable));
            default -> throw new GlobalException(GlobalExceptionCode.BAD_REQUEST);
        };
    }

    public CmnBoardListResponseDTO getAllLikesCmn(Member member, int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "boardPk"));
        return CmnBoardListResponseDTO.of(boardRepository.findByMyLikes(member, pageable));
    }

    public CmnBoardListResponseDTO getAllMyCmn(Member member, int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "boardPk"));
        return CmnBoardListResponseDTO.of(boardRepository.findByMyCmn(member, pageable));
    }



    public boolean cmnLike(Member member, OeBoard board){
        Optional<OeBoardLike> boardLike = boardLikeRepository.findByBoardAndMember(board, member);

        if(boardLike.isPresent()){
            boardLikeRepository.delete(boardLike.get());
            boardRepository.updateMinusLike(board.getBoardPk());
            return false;
        }else{
            boardLikeRepository.save(OeBoardLike.builder()
                            .board(board)
                            .member(member)
                    .build());
            boardRepository.updatePlusLike(board.getBoardPk());
            return true;
        }

    }

    public void plusView(long board){
        boardRepository.updatePlusView(board);
    }

}
