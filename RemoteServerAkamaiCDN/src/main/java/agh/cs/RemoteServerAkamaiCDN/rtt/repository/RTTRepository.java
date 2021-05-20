package agh.cs.RemoteServerAkamaiCDN.rtt.repository;

import agh.cs.RemoteServerAkamaiCDN.rtt.domain.RTTEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface RTTRepository extends JpaRepository<RTTEntity, Long> {
    List<RTTEntity> getAllByStartDateIsAfterAndEndDateIsBeforeOrderByStartDate(Date startDate, Date endDate);
}
