package com.OEzoa.OEasy.domain.aioe;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "`ai oe`", schema = "oeasy")
public class AiOe {
    @Id
    @Column(name = "chatbot_pk", nullable = false)
    private Long id;

    @Column(name = "user_pk", nullable = false)
    private String userPk;

    @Column(name = "text")
    private String text;

    @Column(name = "date", nullable = false)
    private LocalDate date;

}