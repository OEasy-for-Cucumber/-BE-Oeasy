package com.OEzoa.OEasy.domain.tip;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "oe_tip", schema = "oeasy")
public class OeTip {
    @Id
    @Column(name = "TIP_PK", nullable = false)
    private Long id;

    @Column(name = "CONTENT", nullable = false, length = 50)
    private String content;

    @Column(name = "WRITER", nullable = false, length = 20)
    private String writer;

    @Column(name = "TIP_YEAR", nullable = false)
    private Integer tipYear;

}