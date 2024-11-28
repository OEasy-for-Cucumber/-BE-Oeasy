package com.OEzoa.OEasy.domain.graph;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "oe_graph_region", schema = "oeasy")
public class OeGraphRegion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date", nullable = false)
    private String date;

    @Column(name = "price", nullable = false)
    private Long price; // 가격

    @Column(name = "region", nullable = false)
    private String region; // 지역명
}
