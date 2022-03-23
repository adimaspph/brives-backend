package test.bta.brivesc09.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
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
@Table(name = "Staff")
@JsonIgnoreProperties(value={"user","listJadwal","log"},allowSetters = true)
public class StaffModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idStaff;

    @NotNull
    @Size(max = 50)
    @Column(name="no_pegawai")
    private String noPegawai;

    @NotNull
    @Column(name="tarif")
    private Integer tarif;

    @ManyToMany(mappedBy = "listStaff")
    List<MapelModel> listMapel;

    @OneToOne(mappedBy = "staff", orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserModel user;

    //Jadwal
    @OneToMany(mappedBy="staff")
    private List<JadwalModel> listJadwal;

    //Log
    @OneToMany(mappedBy="staff")
    private List<LogModel> log;
}