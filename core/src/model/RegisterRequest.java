package model;

public class RegisterRequest {
    private String usuario;
    private String contrasena;
    private String correo;

    public RegisterRequest(String usuario, String contrasena, String correo) {
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.correo = correo;
    }
}

