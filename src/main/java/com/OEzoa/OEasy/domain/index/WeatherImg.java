package com.OEzoa.OEasy.domain.index;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "weather_img", schema = "oeasy")
public class WeatherImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weather_img_pk")
    private Integer id;

    @Column(name="type", nullable = false)
    private String type;

    @Column(name = "oe_word")
    private String oeWord;

    @Column(name="image_url", nullable = false)
    private String imageUrl;


}
