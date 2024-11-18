package com.OEzoa.OEasy.domain.tip;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder(toBuilder = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "oe_tip_img", schema = "oeasy")
public class OeTipImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "oe_tip_pk", nullable = false)
    private Long id;

    @Column(name = "web_img", length = 255)
    private String webImg;

    @Column(name = "mobile_img", length = 255)
    private String mobileImg;



}