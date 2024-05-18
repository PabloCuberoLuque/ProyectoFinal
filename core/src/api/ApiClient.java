package api;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;

public class ApiClient {
    public static void login(String usuario, String contrasena, final ApiListener listener) {
        Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.POST);
        httpRequest.setUrl("http://localhost:8080/api/login");
        httpRequest.setHeader("Content-Type", "application/json");
        String jsonData = "{\"usuario\":\"" + usuario + "\",\"contrasena\":\"" + contrasena + "\"}";
        httpRequest.setContent(jsonData);

        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                String responseData = httpResponse.getResultAsString();
                // Procesar respuesta del backend
                listener.exito(responseData);
            }

            @Override
            public void failed(Throwable t) {
                listener.error(t);
            }

            @Override
            public void cancelled() {
                listener.error(new Exception("Request cancelled"));
            }
        });
    }

    public static void register(String usuario, String contrasena, String correo, final ApiListener listener) {
        Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.POST);
        httpRequest.setUrl("http://localhost:8080/api/register"); // Reemplaza la URL por la del endpoint de registro
        httpRequest.setHeader("Content-Type", "application/json");
        String jsonData = "{\"usuario\":\"" + usuario + "\",\"contrasena\":\"" + contrasena + "\",\"correo\":\"" + correo + "\"}";
        httpRequest.setContent(jsonData);

        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                String responseData = httpResponse.getResultAsString();
                listener.exito(responseData);
            }

            @Override
            public void failed(Throwable t) {
                listener.error(t); // Llamar al método onError en caso de error en la solicitud
            }
            @Override
            public void cancelled() {
                // Manejar la cancelación de la solicitud si es necesario
            }
        });
    }
}