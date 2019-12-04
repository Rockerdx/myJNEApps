package online.jne.com.jneapps.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CabangResponse {
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("data")
    @Expose
    private ArrayList<Cabang> listCabang = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public ArrayList<Cabang> getListCabang() {
        return listCabang;
    }

    public void setListCabang(ArrayList<Cabang> listCabang) {
        this.listCabang = listCabang;
    }

}
