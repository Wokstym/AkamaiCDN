package agh.cs.backendAkamaiCDN.ping.repository;

import agh.cs.backendAkamaiCDN.ping.entity.RTTEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface RTTRepository extends JpaRepository<RTTEntity, Long> {
    List<RTTEntity> getAllByStartDateIsAfterAndEndDateIsBeforeOrderByStartDate(Date startDate, Date endDate);
}
