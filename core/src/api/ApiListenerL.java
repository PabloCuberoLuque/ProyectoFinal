package api;

public interface ApiListenerL<T> {
    void exito(T respuesta);
    void error(Throwable error);
}