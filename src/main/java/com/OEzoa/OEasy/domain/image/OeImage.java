package com.OEzoa.OEasy.domain.image;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "oe_image", schema = "oeasy")
public class OeImage {
    @Id
    @Column(name = "Image_pk", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "ramdom_number", nullable = false)
    private Long ramdomNumber;

    @Size(max = 255)
    @NotNull
    @Column(name = "Image_url", nullable = false)
    private String imageUrl;

}