package umn.ac.id.uastokobako;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerBarang;
    FirebaseDatabase databaseTokobako;
    DatabaseReference sembakoref;
    StorageReference storagesembakoref;
    FirebaseRecyclerOptions<Beras> options;
    FirebaseRecyclerAdapter<Beras,BarangViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataTampil();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.buttonLogout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                break;

            case R.id.buttonKeranjang:
                break;

            case R.id.buttonAdminTambah:
                Intent intent2 = new Intent(MainActivity.this, TambahBarangDuls.class);
                startActivity(intent2);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void dataTampil(){
        recyclerBarang = (RecyclerView)findViewById(R.id.recBarangTampil);
        recyclerBarang.setLayoutManager(new GridLayoutManager(this,2));

        databaseTokobako = FirebaseDatabase.getInstance();
        sembakoref = databaseTokobako.getReference("Beras");
        storagesembakoref = FirebaseStorage.getInstance().getReference("Beras");

        options = new FirebaseRecyclerOptions.Builder<Beras>()
                .setQuery(sembakoref,Beras.class).build();

        adapter = new FirebaseRecyclerAdapter<Beras, BarangViewHolder>(options) {
            @Override
            protected void onBindViewHolder(BarangViewHolder holder, int position, Beras model) {
                holder.namaTampil.setText(model.getNamaBarang());
                holder.hargaTampil.setText(String.valueOf(model.getHarga()));
                holder.stokTampil.setText(String.valueOf(model.getStok()));

                Picasso.get().load(model.getGambarUrl()).into(holder.gambarTampil);

                Animation animation = AnimationUtils.loadAnimation(getBaseContext(), android.R.anim.slide_in_left);
                holder.itemView.startAnimation(animation);
            }

            @NonNull
            @Override
            public BarangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(getBaseContext()).inflate(R.layout.grid_layout,parent,false);
                return new BarangViewHolder(itemView);
            }

        };

        adapter.startListening();
        recyclerBarang.setAdapter(adapter);

    }
}
