package test.bta.brivesc09.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Setter @Getter
@Entity
@Table(name = "log")
@JsonIgnoreProperties(value={"staff"},allowSetters = true)
public class LogModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLog;

    @NotNull
    @Column(nullable = false)
    private String catatan;

    @NotNull
    @Column(nullable = false)
    private String statusKehadiran;

    //Jadwal
    @OneToOne
    @JoinColumn(name = "id_jadwal", referencedColumnName = "idJadwal")
    private JadwalModel jadwal;

    //Staff
    @ManyToOne
    @JoinColumn(name="id_staff", nullable=false)
    private StaffModel staff;

    //Bukti Image
}