package d.osvaldo.tarea2.model;

/**
 * Created by osval on 9/04/18.
 */

public class Usuarios {
    private String id, nombre, password, privilegio;

    public Usuarios() {
    }

    public Usuarios(String id, String nombre, String password, String privilegio) {
        this.id = id;
        this.nombre = nombre;
        this.password = password;
        this.privilegio = privilegio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrivilegio() {
        return privilegio;
    }

    public void setPrivilegio(String privilegio) {
        this.privilegio = privilegio;
    }
}
