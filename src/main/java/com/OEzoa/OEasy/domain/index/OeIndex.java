package com.OEzoa.OEasy.domain.index;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "oe_index", schema = "oeasy")
public class OeIndex {
    @Id
    @Column(name = "INDEX_PK", nullable = false)
    private Long id;

    @Column(name = "TEMPERATURE", nullable = false)
    private Integer temperature;

    @Column(name = "WEATHER", nullable = false, length = 10)
    private String weather;

    @Column(name = "INDEX_TIMESTAMP", nullable = false)
    private Instant indexTimestamp;

}