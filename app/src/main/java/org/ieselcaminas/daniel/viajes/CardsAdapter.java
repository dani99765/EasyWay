package org.ieselcaminas.daniel.viajes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.TarjViewHolder> {

    private static ArrayList<Tarjeta> items;
    private View.OnClickListener listener;
    private RecyclerViewOnItemClickListener recyclerViewOnItemClickListener;

    public CardsAdapter(ArrayList<Tarjeta> items,  RecyclerViewOnItemClickListener recyclerViewOnItemClickListener) {
        this.recyclerViewOnItemClickListener=recyclerViewOnItemClickListener;
        this.items = items;
    }


    public class TarjViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textInicial;
        private TextView textFinal;
        private TextView textLocation;
        private ImageButton imageDelete;
        private FirebaseDatabase database;
        private FirebaseUser user;
        private FirebaseAuth mAuth;

        public TarjViewHolder(View itemView) {
            super(itemView);
            textInicial = (TextView) itemView.findViewById(R.id.textPuntoInicial);
            textFinal = (TextView) itemView.findViewById(R.id.textPuntoFinal);
            textLocation = (TextView) itemView.findViewById(R.id.textLocation);
            imageDelete = (ImageButton) itemView.findViewById(R.id.imageDelete);
            itemView.setOnClickListener(this);

        }

        public void bindTitular(Tarjeta t) {
            textInicial.setText(t.getTextInicial());
            textFinal.setText(t.getTextFinal());
            textLocation.setText(t.getTextLocation());

            mAuth = FirebaseAuth.getInstance();
            user = mAuth.getCurrentUser();
            database = FirebaseDatabase.getInstance();
            final DatabaseReference rutes = database.getReference(user.getUid()+"/Rutes");

            imageDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    items.remove(pos);
                    notifyItemRemoved(pos);
                    rutes.child(String.valueOf(pos)).setValue(null);
                }
            });
        }

        @Override
        public void onClick(View v) {
            recyclerViewOnItemClickListener.onClick(v, getAdapterPosition());
        }
    }

    @Override
    public TarjViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        TarjViewHolder tvh = new TarjViewHolder(itemView);
        return tvh;
    }

    @Override
    public void onBindViewHolder(TarjViewHolder viewHolder, int pos) {
        Tarjeta item = items.get(pos);
        viewHolder.bindTitular(item);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}