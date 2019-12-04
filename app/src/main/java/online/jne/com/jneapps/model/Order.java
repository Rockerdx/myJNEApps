package online.jne.com.jneapps.model;

public class Order {
    String tanggal;
    boolean status;
    String kantorJne;
    String berat;

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getKantorJne() {
        return kantorJne;
    }

    public void setKantorJne(String kantorJne) {
        this.kantorJne = kantorJne;
    }

    public String getBerat() {
        return berat;
    }

    public void setBerat(String berat) {
        this.berat = berat;
    }
}
