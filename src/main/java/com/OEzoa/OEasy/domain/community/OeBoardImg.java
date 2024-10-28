package com.OEzoa.OEasy.domain.community;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
}
