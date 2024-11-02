package com.OEzoa.OEasy.domain.tip;

import com.OEzoa.OEasy.application.tip.OeTipTitleDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder(toBuilder = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "oe_tip_title", schema = "oeasy")
public class OeTipTitle {
    @Id @Column(name="oe_tip_title_pk", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "oe_tip_pk")
    private OeTip oeTip;

    @Column(name="order_index", nullable = false)
    private Integer orderIndex;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name= "color", nullable = false)
    private String color;

    public static List<OeTipTitleDTO> of(List<OeTipTitle> oeTipTitles) {
        List<OeTipTitleDTO> oeTipTitleDTOList = new ArrayList<>();
        for (OeTipTitle oeTipTitle : oeTipTitles) {
            oeTipTitleDTOList.add(new OeTipTitleDTO(oeTipTitle.getContent(), oeTipTitle.getColor(), oeTipTitle.getOrderIndex()));
        }
        return oeTipTitleDTOList;
    }

}
