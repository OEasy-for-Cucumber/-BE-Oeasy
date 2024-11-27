package com.OEzoa.OEasy.domain.community;

import com.OEzoa.OEasy.application.community.DTO.Cmn.CmnBoardListResponseDTO;
import com.OEzoa.OEasy.domain.member.Member;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<OeBoard, Long> {

//    @Query(value = "SELECT b.board_pk AS boardPk, " +
//            "b.title AS title, " +
//            "b.view_cnt AS viewCnt, " +
//            "b.like_cnt AS likeCnt, " +
//            "m.nick_name AS nickname, " +
//            "bi.s3_img_address AS thumbnailUrl " +
//            "FROM oe_board b " +
//            "JOIN member m ON b.member_pk = m.member_pk " +
//            "LEFT JOIN ( " +
//            "    SELECT board_pk, s3_img_address " +
//            "    FROM oe_board_img " +
//            "    WHERE board_img_pk IN ( " +
//            "        SELECT MIN(board_img_pk) " +
//            "        FROM oe_board_img " +
//            "        GROUP BY board_pk " +
//            "    ) " +
//            ") AS bi ON bi.board_pk = b.board_pk " +
//            "WHERE b.title LIKE CONCAT('%', :title, '%')",
//            countQuery = "SELECT COUNT(*) " +
//                    "FROM oe_board b " +
//                    "WHERE b.title LIKE CONCAT('%', :title, '%')",
//            nativeQuery = true)
//    Page<CmnBoardListResponseDTO> findByTitle(@Param("title") String title, Pageable pageable);
    //제목 검색
    @Query("SELECT new com.OEzoa.OEasy.application.community.DTO.Cmn.CmnBoardListResponseDTO(" +
            "b.boardPk, " +
            "b.title, " +
            "b.viewCnt, " +
            "b.likeCnt, " +
            "m.nickname, " +
            "(SELECT bi.s3ImgAddress " +
            " FROM OeBoardImg bi " +
            " WHERE bi.boardImgPk = (SELECT MIN(bi2.boardImgPk) " +
            "                                        FROM OeBoardImg bi2 " +
            "                                        WHERE bi2.board = b)) " +
            ") " +
            "FROM OeBoard b " +
            "JOIN b.member m " +
            "WHERE b.title LIKE CONCAT('%', :title, '%')")
    Page<CmnBoardListResponseDTO> findByTitle(@Param("title") String title, Pageable pageable);
    //제목 & 내용 검색
    @Query("SELECT new com.OEzoa.OEasy.application.community.DTO.Cmn.CmnBoardListResponseDTO(" +
            "b.boardPk, " +
            "b.title, " +
            "b.viewCnt, " +
            "b.likeCnt, " +
            "m.nickname, " +
            "(SELECT bi.s3ImgAddress " +
            " FROM OeBoardImg bi " +
            " WHERE bi.boardImgPk = (SELECT MIN(bi2.boardImgPk) " +
            "                                        FROM OeBoardImg bi2 " +
            "                                        WHERE bi2.board = b)) " +
            ") " +
            "FROM OeBoard b " +
            "JOIN b.member m " +
            "WHERE (b.title LIKE CONCAT('%', :keyword, '%') OR b.content LIKE CONCAT('%', :keyword, '%'))")
    Page<CmnBoardListResponseDTO> findByTitleOrContent(@Param("keyword") String keyword, Pageable pageable);

    //닉네임 검색
    @Query("SELECT new com.OEzoa.OEasy.application.community.DTO.Cmn.CmnBoardListResponseDTO(" +
            "b.boardPk, " +
            "b.title, " +
            "b.viewCnt, " +
            "b.likeCnt, " +
            "m.nickname, " +
            "(SELECT bi.s3ImgAddress " +
            " FROM OeBoardImg bi " +
            " WHERE bi.boardImgPk = (SELECT MIN(bi2.boardImgPk) " +
            "                                        FROM OeBoardImg bi2 " +
            "                                        WHERE bi2.board = b)) " +
            ") " +
            "FROM OeBoard b " +
            "JOIN b.member m " +
            "WHERE (m.nickname LIKE CONCAT('%', :keyword, '%') )")
    Page<CmnBoardListResponseDTO> findByNickname(@Param("keyword") String keyword, Pageable pageable);

    Optional<OeBoard> findByMemberAndBoardPk(Member member,OeBoard boardPk);

    @Modifying
    @Query("UPDATE OeBoard SET viewCnt = viewCnt+1 WHERE boardPk= :board")
    void updatePlusView(long board);
}
