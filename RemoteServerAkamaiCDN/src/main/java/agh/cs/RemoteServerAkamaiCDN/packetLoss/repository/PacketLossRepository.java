package agh.cs.RemoteServerAkamaiCDN.packetLoss.repository;

import agh.cs.RemoteServerAkamaiCDN.packetLoss.domain.PacketLossEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface PacketLossRepository extends JpaRepository<PacketLossEntity, Long> {
    List<PacketLossEntity> getAllByStartDateIsAfterAndEndDateIsBeforeOrderByStartDate(Date startDate, Date endDate);


}
