package agh.cs.backendAkamaiCDN.throughput.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Setter
@Getter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ThroughputEntity {
    public Date startDate;
    public Date endDate;
    public long max;
    public long min;
    public long avg;
    public String host;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
