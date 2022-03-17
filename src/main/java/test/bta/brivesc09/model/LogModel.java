package test.bta.brivesc09.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
<<<<<<< HEAD
import org.springframework.format.annotation.DateTimeFormat;
=======
>>>>>>> 4da931eabfa5018cfbdaab83a24636b4fefdb93c

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
<<<<<<< HEAD

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalTime;
=======
import java.io.Serializable;
import java.util.List;
>>>>>>> 4da931eabfa5018cfbdaab83a24636b4fefdb93c

@AllArgsConstructor
@NoArgsConstructor
@Setter @Getter
@Entity
@Table(name = "log")
public class LogModel implements Serializable {

    @Id
<<<<<<< HEAD
    @Column(name = "id_log", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLog;
    
    @NotNull
    @Column(name = "tanggal",nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalTime tanggal;

    @NotNull
    @Column(name = "deskripsi", nullable = false)
    private String deskripsi;

    @NotNull
    @Column(name="status_kehadiran")
    private Integer statusKehadiran;

    @NotNull
    @Column(name="bukti_kehadiran")
    private String buktiKehadiran;

    // relasi dengan pengajar

=======
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLog;

    @NotNull
    @Column(nullable = false)
    private String catatan;

    @NotNull
    @Column(nullable = false)
    private Boolean statusKehadiran;

    //Jadwal
    @OneToOne(mappedBy="log")
    private JadwalModel jadwal;

    //Staff
    @ManyToOne
    @JoinColumn(name="id_staff", nullable=false)
    private StaffModel staff;

    //Bukti Image
>>>>>>> 4da931eabfa5018cfbdaab83a24636b4fefdb93c
}