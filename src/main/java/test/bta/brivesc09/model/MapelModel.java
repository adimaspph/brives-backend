package test.bta.brivesc09.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(value={"listStaff"},allowSetters = true)
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


//    @ManyToOne(fetch = FetchType.EAGER, optional = false)
//    @JoinColumn(name = "idJenjang", referencedColumnName = "idJenjang")
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    private JenjangModel jenjang;

    @ManyToMany
    @JoinTable(
            name = "mapel_jenjang",
            joinColumns = @JoinColumn(name = "idMapel"),
            inverseJoinColumns = @JoinColumn(name = "idJenjang"))
    List<JenjangModel> listJenjang;

}