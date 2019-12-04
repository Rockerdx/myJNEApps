package online.jne.com.jneapps.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class HistoryResponse {
        @SerializedName("error")
        @Expose
        private Boolean error;
        @SerializedName("data")
        @Expose
        private ArrayList<History> listCabang = null;

        public Boolean getError() {
            return error;
        }

        public void setError(Boolean error) {
            this.error = error;
        }

        public ArrayList<History> getListHistory() {
            return listCabang;
        }

        public void setListHistory(ArrayList<History> listCabang) {
            this.listCabang = listCabang;
        }
        }
