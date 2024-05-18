package api;

public interface ApiListener {
    void exito(String respuesta); // Método para manejar la respuesta exitosa
    void error(Throwable error);  // Método para manejar errores de la solicitud
}
