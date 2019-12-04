package online.jne.com.jneapps.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import online.jne.com.jneapps.R;
import online.jne.com.jneapps.SuccessActivity;
import online.jne.com.jneapps.helper.Utils;
import online.jne.com.jneapps.model.History;
import online.jne.com.jneapps.model.Order;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    Context context;
    List<History> mValues;

    public OrderAdapter(Context context, List<History> items) {
        this.context = context;
        mValues = items;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final History history = mValues.get(position);

        String status;
        holder.berat.setText("Berat " + history.getKisaranBerat() + "KG");
        holder.kantor.setText(history.getCabang());
        status = history.getStatus();
        if(status.equals("0")){
            holder.status.setText("waiting");
        }
        else if(status.equals("1")){
            holder.status.setText("on process");
        }
        else if(status.equals("2")){
            holder.status.setText("complete");
        }
        else
            holder.status.setText("dibatalkan");
        holder.tanggal.setText(Utils.convertDate(history.getUploadTime()));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent x  = new Intent(context,SuccessActivity.class);
                x.putExtra("order",history);
                x.putExtra("selesai",false);
                x.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(x);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        TextView kantor;
        TextView tanggal;
        TextView berat;
        TextView status;

        ViewHolder(View view) {
            super(view);

            mView = view.findViewById(R.id.order_view);
            kantor = view.findViewById(R.id.kantorjne);
            tanggal = view.findViewById(R.id.tgl_pesan);
            berat = view.findViewById(R.id.berat);
            status = view.findViewById(R.id.status_order);

        }
    }

}