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
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Setter @Getter
@Entity
@Table(name = "Staff")
public class StaffModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idStaff;
    
    @NotNull
    @Size(max = 50)
    @Column(name="email", unique = true)
    private String email;

    @NotNull
    @Lob
    @Column(name="password")
    private String password;

    @NotNull
    @Size(max = 50)
    @Column(name="nama_lengkap")
    private String namaLengkap;

    @NotNull
    @Size(max = 50)
    @Column(name="no_pegawai")
    private String noPegawai;

    @NotNull
    @Size(max = 50)
    @Column(name="no_hp")
    private String noHp;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "idRole", referencedColumnName = "idRole")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private RoleModel role;

}