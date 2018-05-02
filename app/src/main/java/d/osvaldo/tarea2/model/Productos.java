package d.osvaldo.tarea2.model;

/**
 * Created by osval on 23/04/18.
 */

public class Productos {
    private String foto, nombre, precio, id;

    public Productos() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Productos(String foto, String nombre, String precio, String id) {
        this.foto = foto;
        this.nombre = nombre;
        this.precio = precio;
        this.id = id;

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
