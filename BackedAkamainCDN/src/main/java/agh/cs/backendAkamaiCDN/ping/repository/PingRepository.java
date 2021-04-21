package agh.cs.backendAkamaiCDN.ping.repository;

import agh.cs.backendAkamaiCDN.ping.entity.PingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface PingRepository extends JpaRepository<PingEntity, Date> {
    List<PingEntity> getAllByStartDateIsAfterAndEndDateIsBeforeOrderByStartDate(Date startDate, Date endDate);
}
