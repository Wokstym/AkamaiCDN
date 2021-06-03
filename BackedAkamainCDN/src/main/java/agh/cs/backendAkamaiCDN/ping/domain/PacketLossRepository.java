package agh.cs.backendAkamaiCDN.ping.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface PacketLossRepository extends JpaRepository<PacketLossEntity, UUID> {
    List<PacketLossEntity> getAllByStartDateIsAfterAndEndDateIsBeforeOrderByStartDate(Date startDate, Date endDate);
}
