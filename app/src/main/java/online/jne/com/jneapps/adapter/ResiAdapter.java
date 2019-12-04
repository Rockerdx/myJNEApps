package online.jne.com.jneapps.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import online.jne.com.jneapps.R;
import online.jne.com.jneapps.model.Resi;

public class ResiAdapter extends RecyclerView.Adapter<ResiAdapter.ViewHolder> {

    Context context;
    List<Resi> mValues;

    public ResiAdapter(Context context, List<Resi> items) {
        this.context = context;
        mValues = items;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.resi_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ResiAdapter.ViewHolder holder, int position) {
        final Resi resi = mValues.get(position);

        holder.resiType.setText(resi.getService());
        holder.noresi.setText("No.Resi : " + resi.getNoResi());
        holder.keterangan.setText(resi.getAsal() + "->" + resi.getTujuan() + " ・ " + resi.getBerat());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("copy", resi.getNoResi());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context,"Resi telah dicopy",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView noresi;
        TextView keterangan;
        TextView resiType;
        ConstraintLayout layout;

        ViewHolder(View view) {
            super(view);

            layout = view.findViewById(R.id.resi_layout);
            noresi = view.findViewById(R.id.resi_no);
            keterangan = view.findViewById(R.id.resi_tanggal);
            resiType = view.findViewById(R.id.resitype);

        }
    }

}