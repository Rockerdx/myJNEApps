package online.jne.com.jneapps.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cabang implements Parcelable {
    @SerializedName("nama_cabang")
    @Expose
    private String nama;
    @SerializedName("id_cabang")
    @Expose
    private String id_cabang;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lon")
    @Expose
    private String lng;

    public String getId_cabang() {
        return id_cabang;
    }

    public void setId_cabang(String id_cabang) {
        this.id_cabang = id_cabang;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String toString() {
        return nama;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nama);
        dest.writeString(this.id_cabang);
        dest.writeString(this.lat);
        dest.writeString(this.lng);
    }

    public Cabang() {
    }

    protected Cabang(Parcel in) {
        this.nama = in.readString();
        this.id_cabang = in.readString();
        this.lat = in.readString();
        this.lng = in.readString();
    }

    public static final Parcelable.Creator<Cabang> CREATOR = new Parcelable.Creator<Cabang>() {
        @Override
        public Cabang createFromParcel(Parcel source) {
            return new Cabang(source);
        }

        @Override
        public Cabang[] newArray(int size) {
            return new Cabang[size];
        }
    };
}
