package online.jne.com.jneapps.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderResponse {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("kode_order")
    @Expose
    private String kodeOrder;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getKodeOrder() {
        return kodeOrder;
    }

    public void setKodeOrder(String kodeOrder) {
        this.kodeOrder = kodeOrder;
    }

}