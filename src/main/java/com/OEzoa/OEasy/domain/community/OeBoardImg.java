package com.OEzoa.OEasy.domain.community;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "oe_board_img", schema = "oeasy")
public class OeBoardImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_img_pk", nullable = false)
    private Long boardImgPk;

    @Column(name = "s3_img_address", nullable = false)
    private String s3ImgAddress;

    @ManyToOne
    @JoinColumn(name = "board_pk", nullable = false)
    private OeBoard board;

    public static OeBoardImg of(OeBoard board, String url){
        return OeBoardImg.builder()
                    .s3ImgAddress(url)
                    .board(board)
                    .build();
    }
}
