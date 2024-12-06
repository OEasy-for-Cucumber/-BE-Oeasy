package com.OEzoa.OEasy.domain.aioe;

import com.OEzoa.OEasy.domain.member.Member;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AioeUsageId implements Serializable {
    private Member member;
    private LocalDate usageDate;
}
