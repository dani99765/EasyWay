package org.ieselcaminas.daniel.viajes;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by alu20927800p on 02/02/18.
 */

public class Rutes {
    public String inicio;
    public String destino;
    public String location;
    public String uid;

    public Rutes(String inicio, String destino, String location, String uid) {
        this.inicio = inicio;
        this.destino = destino;
        this.location = location;
        this.uid=uid;
    }

    public String getInicio() {
        return inicio;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public String getDestino() {
        return destino;
    }

    public String getUid() {
        return uid;
    }


    public void setDestino(String destino) {
        this.destino = destino;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
