package test.bta.brivesc09.rest;

import java.util.List;

import test.bta.brivesc09.model.RoleModel;
import test.bta.brivesc09.model.SiswaModel;
import test.bta.brivesc09.model.StaffModel;
import test.bta.brivesc09.model.UserModel;

public class StaffDTO {
    public String username;
    public String namaLengkap;
    public String email;
    public String password;
    public String noHP;
    public String role;

    public String noPegawai;
    public Integer tarif;
    public List<String> listMapel;

    public String getUsername() {
        return this.username;
    }

    public String getNamaLengkap() {
        return this.namaLengkap;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public String getNoHP() {
        return this.noHP;
    }

    public String getRole() {
        return this.role;
    }

    public String getNoPegawai() {
        return this.noPegawai;
    }

    public Integer getTarif() {
        return this.tarif;
    }

    public List<String> getListMapel() {
        return this.listMapel;
    }
}