package com.OEzoa.OEasy.application.index;

import com.OEzoa.OEasy.domain.index.Weather;
import com.OEzoa.OEasy.domain.index.WeatherImgRepository;
import com.OEzoa.OEasy.domain.index.WeatherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IndexService {

    private final WeatherRepository weatherRepository;
    private final WeatherImgRepository weatherImgRepository;

    public OEIndexDTO getOEIndex(){
        Weather weather = weatherRepository.findTopByOrderByIdDesc().get();
        return Weather.of(weather);
    }

}
