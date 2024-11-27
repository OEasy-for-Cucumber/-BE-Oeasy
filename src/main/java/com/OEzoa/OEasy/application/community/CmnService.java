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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
        
        if(!cmn.getImgList().isEmpty()){
            List<OeBoardImg> urlList = new ArrayList<>();
            for (MultipartFile multipartFile : cmn.getImgList()) {
                String uniqueImageKey = member.getNickname() + "_" + UUID.randomUUID();
                urlList.add(OeBoardImg.of(board, fileUploader.uploadFile(multipartFile, uniqueImageKey)));
            }
            boardImgRepository.saveAll(urlList);
        }
    }

    public CmnDTOResponse getCmn(OeBoard board, Member member){
        Optional<OeBoardLike> oeBoardLike = boardLikeRepository.findByBoardAndMember(board, member);
        if(oeBoardLike.isPresent()){
            return CmnDTOResponse.of(board, true);
        }else return CmnDTOResponse.of(board, false);
    }

    // s3버킷 리팩토링할 것
    public void updateCmn(OeBoard board, CmnUpdateRequestDTO dto){
        board.toBuilder().title(dto.getTitle())
                .content(dto.getContent())
                .build();
        boardRepository.save(board);
        //----이미지
        for (OeBoardImg image : board.getImages()) {
            fileUploader.deleteImage(image.getS3ImgAddress());
        }
        boardImgRepository.deleteByBoard(board);
        for(MultipartFile multipartFile : dto.getImgList()){
            OeBoardImg oeBoardImg = OeBoardImg.builder()
                    .board(board)
                    .s3ImgAddress(fileUploader.uploadFile(multipartFile, multipartFile.getName()))
                    .build();
            boardImgRepository.save(oeBoardImg);
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
    public void testCnt(Long boardId){
        System.out.println("boardId = " + boardId);
        boardLikeRepository.countByBoard(boardRepository.findById(boardId).get());
    }

    public void notFoundTest(){
        List<Member> memberList = memberRepository.findAll();
        for(Member member : memberList){
            System.out.println("member pk = "+member.getMemberPk()+"\ncnt = "+boardLikeRepository.countByMember(member));
        }
    }

    public Page<CmnBoardListResponseDTO> searchBoard(CmnBoardListRequestDTO dto){

        org.springframework.data.domain.Pageable pageable;
        if(dto.isSortType()) {
            pageable = PageRequest.of(dto.getPage(), dto.getSize(), Sort.by(Sort.Direction.ASC, dto.getSortKeyword()));
        }else{
            pageable = PageRequest.of(dto.getPage(), dto.getSize(), Sort.by(Sort.Direction.DESC, dto.getSortKeyword()));
        }

        return switch (dto.getSearchType()) {
            case "titleAndContent" -> boardRepository.findByTitleOrContent(dto.getSearchKeyword(), pageable);
            case "title" -> boardRepository.findByTitle(dto.getSearchKeyword(), pageable);
            case "nickname" -> boardRepository.findByNickname(dto.getSearchKeyword(), pageable);
            default -> throw new GlobalException(GlobalExceptionCode.BAD_REQUEST);
        };
    }

    public boolean cmnLike(Member member, OeBoard board){
        Optional<OeBoardLike> boardLike = boardLikeRepository.findByBoardAndMember(board, member);

        if(boardLike.isPresent()){
            boardLikeRepository.delete(boardLike.get());
            return false;
        }else{
            boardLikeRepository.save(OeBoardLike.builder()
                            .board(board)
                            .member(member)
                    .build());
            return true;
        }

    }

    public void plusView(OeBoard board){
        boardRepository.updatePlusView(board);
    }

}
