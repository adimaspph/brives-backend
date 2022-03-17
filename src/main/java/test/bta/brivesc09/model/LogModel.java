package test.bta.brivesc09.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Setter @Getter
@Entity
@Table(name = "log")
public class LogModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLog;

    @NotNull
    @Column(nullable = false)
    private String catatan;

    @NotNull
    @Column(nullable = false)
    private Boolean statusKehadiran;

    //Jadwal
    @OneToOne(mappedBy="log")
    private JadwalModel jadwal;

    //Staff
    @ManyToOne
    @JoinColumn(name="id_staff", nullable=false)
    private StaffModel staff;

    //Bukti Image
}