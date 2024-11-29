package com.OEzoa.OEasy.domain.community;


import com.OEzoa.OEasy.application.community.DTO.CmnComment.GetAllCommentDTO;
import com.OEzoa.OEasy.application.community.DTO.CmnComment.GetAllCommentResponseDTO;
import com.OEzoa.OEasy.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardCommentRepository extends JpaRepository<OeBoardComment, Long> {


    @Query("SELECT new com.OEzoa.OEasy.application.community.DTO.CmnComment.GetAllCommentDTO(" +
            "member.memberPk, " +
            "boardCommentPk, " +
            "content, " +
            "member.nickname, " +
            "boardCommentTimestamp )" +
            "FROM OeBoardComment " +
            "WHERE board = :board")
    Page<GetAllCommentDTO> findByBoard(OeBoard board, Pageable pageable);

    Optional<OeBoardComment> findByBoardCommentPkAndMember(long boardComment, Member member);
}
