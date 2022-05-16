package test.bta.brivesc09.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter @Getter
@Entity
@Table(name = "siswa")
@JsonIgnoreProperties(value={"user", "listJadwal", "listPesanan"},allowSetters = true)
// jangan uncomment list jadwal/ list pesanan agar tidak infinite loop
public class SiswaModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSiswa;

    @NotNull
    @Size(max = 50)
    @Column(name="asal_sekolah")
    private String asalSekolah;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_jenjang", referencedColumnName = "idJenjang", nullable = false)
    private JenjangModel jenjang;

    @OneToOne(mappedBy = "siswa")
    private UserModel user;

    //Jadwal
    @OneToMany(mappedBy="siswa")
    private List<JadwalModel> listJadwal;

    //Pesanan
    @OneToMany(mappedBy="siswa")
    private List<PesananModel> listPesanan;

    
}