package com.kbhc.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customer_record")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class CustomerRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recordKey;
    private String memo;

    @OneToMany(mappedBy = "record", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecordEntry> entries = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdDate;

    @Builder
    CustomerRecord(Long id, String recordKey, String memo, List<RecordEntry> entries, LocalDateTime createdDate) {
        this.id = id;
        this.recordKey = recordKey;
        this.memo = memo;
        this.entries = entries != null ? entries : new ArrayList<>();
        this.createdDate = createdDate;
    }
}
