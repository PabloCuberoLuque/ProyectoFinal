package model;

public class Jugador {
    private int id;
    private String usuario;
    private String contrasena;
    private String correo;

    public Jugador(int id, String usuario, String contrasena, String correo) {
        this.id = id;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.correo = correo;
    }

    public Jugador() {
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
