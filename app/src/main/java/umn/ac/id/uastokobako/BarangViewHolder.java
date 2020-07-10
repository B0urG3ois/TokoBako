package umn.ac.id.uastokobako;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class BarangViewHolder extends RecyclerView.ViewHolder {
    TextView namaTampil, hargaTampil, stokTampil;
    ImageView gambarTampil;
    public BarangViewHolder(View itemView) {
        super(itemView);

        namaTampil = (TextView)itemView.findViewById(R.id.namaBarangTampil);
        hargaTampil = (TextView)itemView.findViewById(R.id.hargaBarangTampil);
        stokTampil = (TextView)itemView.findViewById(R.id.stokBarangTampil);
        gambarTampil = (ImageView)itemView.findViewById(R.id.gambarBarang);
    }
}
