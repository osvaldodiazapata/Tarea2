package d.osvaldo.tarea2.model;

/**
 * Created by osval on 27/05/18.
 */

public class Pedidos {
    private String nombre, cantidad, precio_u;

    public Pedidos() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getPrecio_u() {
        return precio_u;
    }

    public void setPrecio_u(String precio_u) {
        this.precio_u = precio_u;
    }

    public Pedidos(String nombre, String cantidad, String precio_u) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio_u = precio_u;
    }
}
