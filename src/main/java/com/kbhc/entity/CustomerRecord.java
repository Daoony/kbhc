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
    private Long customerRecordId;

    private String recordKey;
    private String memo;

    @OneToMany(mappedBy = "customerRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecordEntry> recordEntries = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdDate;

    @Builder
    CustomerRecord(Long customerRecordId, String recordKey, String memo, List<RecordEntry> recordEntries, LocalDateTime createdDate) {
        this.customerRecordId = customerRecordId;
        this.recordKey = recordKey;
        this.memo = memo;
        this.recordEntries = recordEntries != null ? recordEntries : new ArrayList<>();
        this.createdDate = createdDate;
    }
}
