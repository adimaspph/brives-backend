package test.bta.brivesc09.model;

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

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter @Getter
@Entity
@Table(name = "log")
public class LogModel implements Serializable {

    @Id
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

}