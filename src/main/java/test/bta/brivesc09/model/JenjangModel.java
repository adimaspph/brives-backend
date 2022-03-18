package test.bta.brivesc09.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Entity
@Getter
@Table(name = "jenjang")
@JsonIgnoreProperties(value={"listMapel"},allowSetters = true)
public class JenjangModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idJenjang;

    @NotNull
    @Size(max = 50)
    @Column(name = "nama_jenjang", nullable = false)
    private String namaJenjang;

    @ManyToMany(mappedBy = "listJenjang")
    private List<MapelModel> listMapel;

}
