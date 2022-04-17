package test.bta.brivesc09.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter @Getter
@Entity
@Table(name = "pesanan")
public class PesananModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPesanan;

    @NotNull
    @Column(nullable = false)
    private String materi;

    @NotNull
    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime tanggal_dibuat;

    @NotNull
    @Column(nullable = false)
    private Integer nominal;

    // bukti image
    @NotNull
    @Column(nullable = false)
    private String bukti;

    //Jadwal
    @ManyToOne
    @JoinColumn(name="id_jadwal", nullable=false)
    private JadwalModel jadwal;

    //Siswa
    @ManyToOne
    @JoinColumn(name="id_siswa", nullable=false)
    private SiswaModel siswa;

    //Status
    @ManyToOne
    @JoinColumn(name="id_status", nullable=false)
    private StatusPesananModel status;
}