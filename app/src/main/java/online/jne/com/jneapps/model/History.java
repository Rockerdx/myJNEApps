package online.jne.com.jneapps.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class History implements Parcelable {
    @SerializedName("kode_order")
    @Expose
    private String kodeOrder;
    @SerializedName("cabang")
    @Expose
    private String cabang;
    @SerializedName("upload_time")
    @Expose
    private String uploadTime;
    @SerializedName("alamat")
    @Expose
    private String alamat;
    @SerializedName("kisaran_berat")
    @Expose
    private String kisaranBerat;
    @SerializedName("keterangan")
    @Expose
    private String keterangan;
    @SerializedName("status")
    @Expose
    private String status;

    public String getKodeOrder() {
        return kodeOrder;
    }

    public void setKodeOrder(String kodeOrder) {
        this.kodeOrder = kodeOrder;
    }

    public String getCabang() {
        return cabang;
    }

    public void setCabang(String cabang) {
        this.cabang = cabang;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getKisaranBerat() {
        return kisaranBerat;
    }

    public void setKisaranBerat(String kisaranBerat) {
        this.kisaranBerat = kisaranBerat;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.kodeOrder);
        dest.writeString(this.cabang);
        dest.writeString(this.uploadTime);
        dest.writeString(this.alamat);
        dest.writeString(this.kisaranBerat);
        dest.writeString(this.keterangan);
        dest.writeString(this.status);
    }

    public History() {
    }

    protected History(Parcel in) {
        this.kodeOrder = in.readString();
        this.cabang = in.readString();
        this.uploadTime = in.readString();
        this.alamat = in.readString();
        this.kisaranBerat = in.readString();
        this.keterangan = in.readString();
        this.status = in.readString();
    }

    public static final Parcelable.Creator<History> CREATOR = new Parcelable.Creator<History>() {
        @Override
        public History createFromParcel(Parcel source) {
            return new History(source);
        }

        @Override
        public History[] newArray(int size) {
            return new History[size];
        }
    };
}