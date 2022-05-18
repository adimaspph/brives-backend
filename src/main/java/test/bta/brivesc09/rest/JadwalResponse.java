package test.bta.brivesc09.rest;

import test.bta.brivesc09.model.JadwalModel;
import test.bta.brivesc09.model.StatusPesananModel;
import test.bta.brivesc09.model.UserModel;

public class JadwalResponse {
    public JadwalModel jadwal;
    public UserModel siswa;
    public String statusPesanan = "Belum dipesan";
    public String materi;
}
