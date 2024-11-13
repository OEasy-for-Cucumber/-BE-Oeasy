package com.OEzoa.OEasy.application.tip;

import com.OEzoa.OEasy.domain.tip.OeTipImg;
import com.OEzoa.OEasy.domain.tip.OeTipImgRepository;
import com.OEzoa.OEasy.exception.GlobalException;
import com.OEzoa.OEasy.exception.GlobalExceptionCode;
import com.OEzoa.OEasy.util.timeTrace.TimeTrace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@TimeTrace
public class TipService {

    private final OeTipImgRepository oeTipRepository;
    public OeTipImg getTip() {
        return oeTipRepository.findRandomOeTip().orElseThrow(() -> new GlobalException(GlobalExceptionCode.DB_LOAD_FAILURE));
    }

}
