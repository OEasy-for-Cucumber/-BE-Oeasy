package com.OEzoa.OEasy.domain.index;

import com.OEzoa.OEasy.application.index.OEIndexDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "weather", schema = "oeasy")
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weather_pk")
    private Long id;

    @ManyToOne
    @JoinColumn(name="weather_img_pk", nullable = false)
    private WeatherImg weatherImg;

    @Column(name="temperature", nullable = false)
    private double temperature;

    @Column(name="state")
    private String state;

    @Column(name = "weather_timestamp", nullable = false)
    private LocalDateTime weatherTimestamp;

    public static OEIndexDTO of(Weather weather) {
        return OEIndexDTO.builder()
                .cucumberType(weather.weatherImg.getType())
                .weatherState(weather.state)
                .imgUrl(weather.weatherImg.getImageUrl())
                .temperature(weather.temperature)
                .build();
    }

}