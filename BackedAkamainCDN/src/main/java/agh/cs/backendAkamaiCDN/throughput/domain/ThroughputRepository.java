package agh.cs.backendAkamaiCDN.throughput.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface ThroughputRepository extends JpaRepository<ThroughputEntity, UUID> {
    List<ThroughputEntity> findAllByStartDateIsAfterAndEndDateIsBeforeOrderByStartDate(Date startDate, Date endDate);
}
