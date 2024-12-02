package com.OEzoa.OEasy.domain.aioe;

import com.OEzoa.OEasy.domain.member.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "aioe", schema = "oeasy")
public class AiOe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가
    @Column(name = "aioe_pk", nullable = false)
    private Long aioePk;

    @OneToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "member_pk", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "aiOe", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<AiOeChatMessage> chatMessages;
}
