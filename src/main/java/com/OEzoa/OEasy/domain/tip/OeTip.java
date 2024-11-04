package com.OEzoa.OEasy.domain.tip;

import com.OEzoa.OEasy.application.tip.OeTipDTO;
import com.OEzoa.OEasy.application.tip.OeTipTitleDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Builder(toBuilder = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "oe_tip", schema = "oeasy")
public class OeTip {
    @Id
    @Column(name = "oe_tip_pk", nullable = false)
    private Long id;

    @Column(name = "content", nullable = false, length = 255)
    private String content;

    public static OeTipDTO of(OeTip oeTip, List<OeTipTitleDTO> oeTipTitleDTOList){
        return OeTipDTO.builder()
                .content(oeTip.getContent())
                .oeTipTitleDTOList(oeTipTitleDTOList)
        .build();
    }

}