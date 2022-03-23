package test.bta.brivesc09.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Entity
@Getter
@Table(name = "mapel")
@JsonIgnoreProperties(value={"listStaff", "listJadwal"},allowSetters = true)
// @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, scope = MapelModel.class, property = "idMapel")
public class MapelModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMapel;

    @NotNull
    @Size(max = 50)
    @Column(name = "nama_mapel", nullable = false)
    private String namaMapel;

    @NotNull
    @Column(name = "deskripsi", nullable = false)
    private String deskripsi;


    @ManyToMany
    @JoinTable(
            name = "mapel_jenjang",
            joinColumns = @JoinColumn(name = "idMapel"),
            inverseJoinColumns = @JoinColumn(name = "idJenjang"))
    List<JenjangModel> listJenjang;

    //Relasi ke staff
    @ManyToMany
    @JoinTable(
            name = "staff_mapel",
            joinColumns = @JoinColumn(name = "idMapel"),
            inverseJoinColumns = @JoinColumn(name = "idStaff"))
    List<StaffModel> listStaff;

    @OneToMany(mappedBy="mapel")
    private List<JadwalModel> listJadwal;

    public Long getIdMapel() {
        return idMapel;
    }

    public void setIdMapel(Long idMapel) {
        this.idMapel = idMapel;
    }

    public String getNamaMapel() {
        return namaMapel;
    }

    public void setNamaMapel(String namaMapel) {
        this.namaMapel = namaMapel;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public List<JenjangModel> getListJenjang() {
        return listJenjang;
    }

    public void setListJenjang(List<JenjangModel> listJenjang) {
        this.listJenjang = listJenjang;
    }

    public List<StaffModel> getListStaff() {
        return listStaff;
    }

    public void setListStaff(List<StaffModel> listStaff) {
        this.listStaff = listStaff;
    }

    public List<JadwalModel> getListJadwal() {
        return listJadwal;
    }

    public void setListJadwal(List<JadwalModel> listJadwal) {
        this.listJadwal = listJadwal;
    }
}