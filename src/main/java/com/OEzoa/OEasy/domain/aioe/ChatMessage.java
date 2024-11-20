package com.OEzoa.OEasy.domain.aioe;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "aioe_chatmessage", schema = "oeasy")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatting_pk", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aioe_pk", nullable = false)
    private AiOe aiOe;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "dateTime", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "type", nullable = false)
    private String type; // "user" or "aioe"
}
