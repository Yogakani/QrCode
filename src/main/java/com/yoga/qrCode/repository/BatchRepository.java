package com.yoga.qrCode.repository;

import com.yoga.qrCode.model.entity.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchRepository extends JpaRepository<Batch, Long> {
    Batch findByBatchId(String batchId);
}
