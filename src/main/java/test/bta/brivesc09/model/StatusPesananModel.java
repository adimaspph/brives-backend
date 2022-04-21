package test.bta.brivesc09.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter @Getter
@Entity
@Table(name = "status_pesanan")
@JsonIgnoreProperties(value={"listPesanan"},allowSetters = true)
public class StatusPesananModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idStatusPesanan;

    @NotNull
    @Column(nullable = false)
    private String jenisStatus;

    @OneToMany(mappedBy="status")
    private List<PesananModel> listPesanan;
}