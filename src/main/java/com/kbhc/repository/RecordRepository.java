package com.kbhc.repository;

import com.kbhc.entity.CustomerRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepository extends JpaRepository<CustomerRecord, Long> {
}
