package agh.cs.backendAkamaiCDN.throughput.repository;

import agh.cs.backendAkamaiCDN.throughput.entity.ThroughputEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ThroughputRepository extends JpaRepository<ThroughputEntity, Long> {
    List<ThroughputEntity> findAllByStartDateIsAfterAndEndDateIsBeforeOrderByStartDate(Date startDate, Date endDate);
}
