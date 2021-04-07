package agh.cs.backendAkamaiCDN.throughput.repository;

import agh.cs.backendAkamaiCDN.throughput.entity.ThroughputEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThroughputRepository extends JpaRepository<ThroughputEntity, Long> {
}
