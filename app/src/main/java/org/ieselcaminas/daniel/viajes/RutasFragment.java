package org.ieselcaminas.daniel.viajes;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.Calendar;

public class RutasFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String punto_inicial;
    private String punto_final;
    private boolean isClicked=true;
    private Calendar calendar;
    private String localizacion;
    private DatabaseReference rutes;


    public RutasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_rutas, container, false);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        rutes = database.getReference(user.getUid()+"/Rutes");
        final RecyclerView recView = (RecyclerView) v.findViewById(R.id.recycler);
        final FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.floatingActionButton);
        fab.setVisibility(View.INVISIBLE);
        calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(), this, year, month, day);

        final ArrayList<Tarjeta> items = new ArrayList<Tarjeta>();


        rutes.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                punto_final = dataSnapshot.child("destino").getValue(String.class);
                punto_inicial = dataSnapshot.child("inicio").getValue(String.class);
                localizacion = dataSnapshot.child("location").getValue(String.class);
                items.add(new Tarjeta(punto_inicial, punto_final, localizacion));
                recView.setHasFixedSize(true);

                CardsAdapter adaptador = new CardsAdapter(items, new RecyclerViewOnItemClickListener() {
                    @Override
                    public void onClick(View v, int position) {
                        if(isClicked) {
                            fab.show();
                            isClicked=false;
                        } else {
                            fab.hide();
                            isClicked=true;
                        }
                    }
                });
                recView.setAdapter(adaptador);
                recView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        return v;

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String dates = dayOfMonth + " - " + (month + 1) + " - " + year;
        Toast.makeText(getActivity(),  getString(R.string.textoToastRuta)+" "+ dates, Toast.LENGTH_LONG).show();

        Calendar sevendayalarm = Calendar.getInstance();

        sevendayalarm.add(Calendar.YEAR, year);
        sevendayalarm.add(Calendar.MONTH, month);
        sevendayalarm.add(Calendar.DAY_OF_MONTH, dayOfMonth);

        Intent intent = new Intent(getActivity(), NotificationService.class);
        intent.putExtra("alarm", sevendayalarm);
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());

        if(pref.getBoolean("option1",true)) {
            getActivity().startService(intent);
        } else {

        }
    }
}
