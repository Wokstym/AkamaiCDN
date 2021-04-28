package agh.cs.backendAkamaiCDN.ping.entity;

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
public class RTTEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date startDate;
    private Date endDate;
    private String host;
    private Double minTime;
    private Double maxTime;
    private Double averageTime;
    private Double standardDeviationTime;
}
