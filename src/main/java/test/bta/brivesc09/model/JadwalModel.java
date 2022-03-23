package test.bta.brivesc09.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

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
import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter @Getter
@Entity
@Table(name = "jadwal")
@JsonIgnoreProperties(value={"staff", "siswa", "log", "listPesanan"},allowSetters = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idJadwal")
public class JadwalModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idJadwal;

    @NotNull
    @Column(nullable = false)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "HH:mm")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime waktuMulai;

    @NotNull
    @Column(nullable = false)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "HH:mm")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime waktuSelesai;

    @NotNull
    @Column(nullable = false)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate tanggal;

//    @OneToOne(mappedBy = "siswa")
//    private UserModel user;

    @Column(nullable = true)
    private String linkZoom;

    //Jenis Kelas
    @Enumerated(EnumType.STRING)
    private JenisKelas jenisKelas;

    // Staff
    @ManyToOne
    @JoinColumn(name="id_staff", nullable=false)
    private StaffModel staff;

    // Mapel
    @ManyToOne
    @JoinColumn(name="id_mapel", nullable=false)
    private MapelModel mapel;

    // Siswa
    @ManyToOne
    @JoinColumn(name="id_siswa", nullable=true)
    private SiswaModel siswa;

    // Log
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_log", referencedColumnName = "idLog")
    private LogModel log;

    // Pesanan
    @OneToMany(mappedBy="jadwal")
    private List<PesananModel> listPesanan;
}

