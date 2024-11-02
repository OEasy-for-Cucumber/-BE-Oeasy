package com.OEzoa.OEasy.application.tip;

import com.OEzoa.OEasy.domain.tip.OeTip;
import com.OEzoa.OEasy.domain.tip.OeTipRepository;
import com.OEzoa.OEasy.domain.tip.OeTipTitle;
import com.OEzoa.OEasy.domain.tip.OeTipTitleRepository;
import com.OEzoa.OEasy.exception.GlobalException;
import com.OEzoa.OEasy.exception.GlobalExceptionCode;
import com.OEzoa.OEasy.util.timeTrace.TimeTrace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@TimeTrace
public class TipService {

    private final OeTipRepository oeTipRepository;
    private final OeTipTitleRepository oeTipTitleRepository;
    public OeTipDTO getTip() {
        OeTip oeTip = oeTipRepository.findRandomOeTip().orElseThrow(() -> new GlobalException(GlobalExceptionCode.DB_LOAD_FAILURE));

        List<OeTipTitle> oeTipTitleList = oeTipTitleRepository.findByOeTipOrderByOrderIndexAsc(oeTip)
                .orElseThrow(() -> new RuntimeException(GlobalExceptionCode.DB_LOAD_FAILURE.getMessage()));
        
        return OeTip.of(oeTip, OeTipTitle.of(oeTipTitleList));
    }

}
