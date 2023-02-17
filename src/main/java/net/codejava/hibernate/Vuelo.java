package net.codejava.hibernate;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "vuelo")
public class Vuelo {

    private String identificador;
    private String aeropuerto_origen;
    private String aeropuerto_destino;
    private String tipo_vuelo;
    private Date fecha_vuelo;
    private Set<Pasaje> pasajes;

    public Vuelo(){

    }
    @Id
    @Column(name = "IDENTIFICADOR")
    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }
    @Column(name = "AEROPUERTO_ORIGEN")
    public String getAeropuerto_origen() {
        return aeropuerto_origen;
    }

    public void setAeropuerto_origen(String aeropuerto_origen) {
        this.aeropuerto_origen = aeropuerto_origen;
    }
    @Column(name = "AEROPUERTO_DESTINO")
    public String getAeropuerto_destino() {
        return aeropuerto_destino;
    }

    public void setAeropuerto_destino(String aeropuerto_destino) {
        this.aeropuerto_destino = aeropuerto_destino;
    }
    @Column(name = "TIPO_VUELO")
    public String getTipo_vuelo() {
        return tipo_vuelo;
    }

    public void setTipo_vuelo(String tipo_vuelo) {
        this.tipo_vuelo = tipo_vuelo;
    }
    @Column(name = "FECHA_VUELO")
    public Date getFecha_vuelo() {
        return fecha_vuelo;
    }

    public void setFecha_vuelo(Date fecha_vuelo) {
        this.fecha_vuelo = fecha_vuelo;
    }
    @OneToMany(mappedBy = "identificador", cascade = CascadeType.ALL)
    public Set<Pasaje> getPasajes() {
        return pasajes;
    }

    public void setPasajes(Set<Pasaje> pasajes) {
        this.pasajes = pasajes;
    }
}
