package com.OEzoa.OEasy.domain.graph;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "oe_graph", schema = "oeasy")
public class OeGraph {

    @Id
    @Column(name = "date", nullable = false)
    private String date;

    @Column(name = "price", nullable = false)
    private Long price;
}
