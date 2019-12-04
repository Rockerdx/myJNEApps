package online.jne.com.jneapps.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DetailResponse {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("driver")
    @Expose
    private Driver driver;
    @SerializedName("resi")
    @Expose
    private List<Resi> resi = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public List<Resi> getResi() {
        return resi;
    }

    public void setResi(List<Resi> resi) {
        this.resi = resi;
    }

}