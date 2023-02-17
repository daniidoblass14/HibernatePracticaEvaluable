package net.codejava.hibernate;

import javax.persistence.*;

@Entity
@Table(name = "pasaje")
public class Pasaje {

    private int cod;
    private Pasajero pasajero_cod;
    private Vuelo identificador;
    private int numAsientos;
    private String clase;
    private float pvp;

    public Pasaje(){

    }

    @Id
    @Column(name = "COD")
    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="PASAJERO_COD")
    public Pasajero getPasajero_cod() {
        return pasajero_cod;
    }

    public void setPasajero_cod(Pasajero pasajero_cod) {
        this.pasajero_cod = pasajero_cod;
    }
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "IDENTIFICADOR")
    public Vuelo getIdentificador() {
        return identificador;
    }

    public void setIdentificador(Vuelo identificador) {
        this.identificador = identificador;
    }
    @Column(name = "NUMASIENTO")
    public int getNumAsientos() {
        return numAsientos;
    }

    public void setNumAsientos(int numAsientos) {
        this.numAsientos = numAsientos;
    }
    @Column(name = "CLASE")
    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }
    @Column(name = "PVP")
    public float getPvp() {
        return pvp;
    }

    public void setPvp(float pvp) {
        this.pvp = pvp;
    }
}
