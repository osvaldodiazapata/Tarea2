package d.osvaldo.tarea2.model;

/**
 * Created by osval on 23/04/18.
 */

public class Productos {
    String foto, nombre, precio;

    public Productos() {
    }

    public Productos(String foto, String nombre, String precio) {
        this.foto = foto;
        this.nombre = nombre;
        this.precio = precio;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }
}
