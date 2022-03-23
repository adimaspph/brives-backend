package test.bta.brivesc09.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Builder
@Table(name = "users")
@JsonIgnoreProperties(value = { "password" }, allowSetters = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idUser")
public class UserModel implements Serializable {

    @Id
    @NotNull
    private Long idUser;

    @NotNull
    @Size(max = 50)
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @NotNull
    @Size(max = 50)
    @Column(name = "nama_lengkap", nullable = false)
    private String namaLengkap;

    @Size(max = 50)
    @Column(name = "email", nullable = true)
    private String email;

    @NotNull
    @Lob
    @Type(type = "text")
    @Column(name = "password", nullable = false, columnDefinition = "TEXT")
    private String password;

    @Size(max = 50)
    @Column(name = "no_HP", nullable = true)
    private String noHP;

    // @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_role", referencedColumnName = "idRole", nullable = false)
    @JsonIgnore
    private RoleModel role;

    // Relasi staff
    // @JsonBackReference
    @OneToOne(cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_staff", referencedColumnName = "idStaff")
    private StaffModel staff;

    // Relasi siswa
    @OneToOne(cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_siswa", referencedColumnName = "idSiswa")
    private SiswaModel siswa;

    // Foto (image)
}
