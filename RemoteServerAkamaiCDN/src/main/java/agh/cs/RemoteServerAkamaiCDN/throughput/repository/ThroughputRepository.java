package agh.cs.RemoteServerAkamaiCDN.throughput.repository;

import agh.cs.RemoteServerAkamaiCDN.throughput.domain.ThroughputEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ThroughputRepository extends JpaRepository<ThroughputEntity, Long> {
    List<ThroughputEntity> findAllByStartDateIsAfterAndEndDateIsBeforeOrderByStartDate(Date startDate, Date endDate);
}
