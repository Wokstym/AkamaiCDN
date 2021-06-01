package agh.cs.backendAkamaiCDN.ping.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface RttRepository extends JpaRepository<RTTEntity, UUID> {
    List<RTTEntity> getAllByStartDateIsAfterAndEndDateIsBefore(Date startDate, Date endDate);
}
