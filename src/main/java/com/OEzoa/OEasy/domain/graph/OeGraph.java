package com.OEzoa.OEasy.domain.graph;

import jakarta.persistence.*;
import java.time.chrono.Chronology;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "oe_graph", schema = "oeasy")
public class OeGraph {

    @Id
    @Column(name = "date", nullable = false)
    private String date;

    @Column(name = "price", nullable = false)
    private Long price;

}
