package api;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.utils.Json;
import model.RegisterRequest;
import scenes.Login;
import scenes.MenuPlayer;

public class ApiClient {
    public static void login(String usuario, String contrasena, final ApiListener listener) {
        Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.POST);
        httpRequest.setUrl("http://localhost:8080/jugador/login"); // URL del endpoint de login
        httpRequest.setHeader("Content-Type", "application/json");

        // Crear el JSON manualmente
        String jsonData = "{\"usuario\":\"" + usuario + "\",\"contrasena\":\"" + contrasena + "\"}";
        httpRequest.setContent(jsonData);

        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                int statusCode = httpResponse.getStatus().getStatusCode();
                String responseData = httpResponse.getResultAsString();
                if (statusCode == 200) {
                    listener.exito(responseData);
                } else {
                    listener.error(new Exception(responseData));
                }
            }

            @Override
            public void failed(Throwable t) {
                listener.error(t);
            }

            @Override
            public void cancelled() {
                // Manejar la cancelación de la solicitud si es necesario
            }
        });
    }


    public static void register(String usuario, String contrasena, String correo, final ApiListener listener) {
        Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.POST);
        httpRequest.setUrl("http://localhost:8080/jugador/register"); // Reemplaza la URL por la del endpoint de registro
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