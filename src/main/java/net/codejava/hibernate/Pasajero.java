package net.codejava.hibernate;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "pasajero")
public class Pasajero {

    private int cod;
    private String nombre;
    private String tlf;
    private String direccion;
    private String pais;
    private Set<Pasaje> pasajes;
    public Pasajero(){

    }

    @Id
    @Column(name = "COD")
    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    @Column(name = "NOMBRE")
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    @Column(name = "TLF")
    public String getTlf() {
        return tlf;
    }

    public void setTlf(String tlf) {
        this.tlf = tlf;
    }
    @Column(name = "DIRECCION")
    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    @Column(name = "PAIS")
    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }
    @OneToMany(mappedBy = "pasajero_cod", cascade = CascadeType.ALL)
    public Set<Pasaje> getPasajes() {
        return pasajes;
    }

    public void setPasajes(Set<Pasaje> pasajes) {
        this.pasajes = pasajes;
    }
}
