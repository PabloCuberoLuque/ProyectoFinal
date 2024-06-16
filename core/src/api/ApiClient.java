package api;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import model.Inventario;
import model.Jugador;
import model.Partida;
import model.RegisterRequest;
import scenes.Login;
import scenes.MenuPlayer;

import java.util.ArrayList;
import java.util.List;

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

    // Método para verificar si un usuario ya existe
    public static void verificarUsuarioExistente(String usuario, ApiListener listener) {
        Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.GET);
        httpRequest.setUrl("http://localhost:8080/jugador/verificarUsuario/"+ usuario); // Reemplaza la URL por la del endpoint de registro
        httpRequest.setHeader("Content-Type", "application/json");


        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                int statusCode = httpResponse.getStatus().getStatusCode();
                if (statusCode == HttpStatus.SC_OK) {
                    String responseData = httpResponse.getResultAsString();
                    boolean usuarioExiste = Boolean.parseBoolean(responseData);
                    listener.exito(String.valueOf(usuarioExiste));
                } else {
                    listener.error(new Throwable("Error al verificar usuario. Código de estado HTTP: " + statusCode));
                }
            }

            @Override
            public void failed(Throwable t) {
                listener.error(t);
            }

            @Override
            public void cancelled() {
                // Handle cancellation if necessary
            }
        });
    }

    public static void crearPartidaEInventario(int idJugador, int idObjeto ,int idPartida, int enemigosAsesinados, final ApiListener listener) {
        crearInventario(idPartida, idObjeto, new ApiListener() {
            @Override
            public void exito(String inventarioResponse) {
                try {
                    JsonValue inventarioJson = new JsonReader().parse(inventarioResponse);
                    int inventarioId = inventarioJson.getInt("id");

                    // Crear la partida con el id del inventario
                    Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.POST);
                    httpRequest.setUrl("http://localhost:8080/partida/create");
                    httpRequest.setHeader("Content-Type", "application/json");


                    String partida = "{\"jugador_id\":\"" + idJugador + "\",\"inventario_id\":\"" + inventarioId + "\",\"enemigosAsesinados\":\"" + enemigosAsesinados + "\"}";
                    httpRequest.setContent(partida);

                    System.out.println(partida);
                    Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
                        @Override
                        public void handleHttpResponse(Net.HttpResponse httpResponse) {
                            if (httpResponse.getStatus().getStatusCode() == HttpStatus.SC_OK) {
                                String responseData = httpResponse.getResultAsString();
                                listener.exito(responseData);
                            } else {
                                listener.error(new Throwable("Error al crear la partida. Código de estado HTTP: " + httpResponse.getStatus().getStatusCode()));
                            }
                        }

                        @Override
                        public void failed(Throwable t) {
                            listener.error(t);
                        }

                        @Override
                        public void cancelled() {
                            // Handle cancellation if necessary
                        }
                    });

                } catch (Exception e) {
                    listener.error(e);
                }
            }

            @Override
            public void error(Throwable error) {
                listener.error(error);
            }
        });
    }

    private static void crearInventario(int idPartida, int idObjeto, final ApiListener listener) {
        Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.POST);
        httpRequest.setUrl("http://localhost:8080/inventario/create");
        httpRequest.setHeader("Content-Type", "application/json");


        String jsonData = "{\"partida_id\":\"" + null + "\",\"objeto_id\":\"" + null + "\"}";
        httpRequest.setContent(jsonData);


        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                if (httpResponse.getStatus().getStatusCode() == HttpStatus.SC_OK) {
                    String responseData = httpResponse.getResultAsString();
                    listener.exito(responseData);
                } else {
                    listener.error(new Throwable("Error al crear el inventario. Código de estado HTTP: " + httpResponse.getStatus().getStatusCode()));
                }
            }

            @Override
            public void failed(Throwable t) {
                listener.error(t);
            }

            @Override
            public void cancelled() {
                // Manejar la cancelación si es necesario
            }
        });
    }


    public static void actualizarEnemigos(int partidaId, int enemigosAsesinados, final ApiListener listener) {
        Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.POST);
        httpRequest.setUrl("http://localhost:8080/partida/actualizarEnemigos");
        httpRequest.setHeader("Content-Type", "application/json");

        String jsonData = "{\"partidaId\":\"" + partidaId + "\",\"enemigosAsesinados\":\"" + enemigosAsesinados + "\"}";
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
    public static void actualizarUsuario(int jugadorId, String usuario,String contrasena, String correo, final ApiListener listener) {
        Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.POST);
        httpRequest.setUrl("http://localhost:8080/jugador/actualizar/"+jugadorId);
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

    public static void obtenerJugadorPorNombre(final ApiListener listener) {
        Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.GET);
        httpRequest.setUrl("http://localhost:8080/jugador/" + AppState.nombreUsuario);
        httpRequest.setHeader("Content-Type", "application/json");

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
    public static void obtenerJugadorPorNombre(String nombre,final ApiListener listener) {
        Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.GET);
        httpRequest.setUrl("http://localhost:8080/jugador/" + nombre);
        httpRequest.setHeader("Content-Type", "application/json");

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

    public static void actualizarInventario(int inventarioId, int objetoId,int partidaId, final ApiListener listener) {
        Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.POST);

        httpRequest.setUrl("http://localhost:8080/inventario/update");
        httpRequest.setHeader("Content-Type", "application/json");

        String jsonData = "{\"inventario_id\":\"" + inventarioId + "\",\"objeto_id\":\"" + objetoId + "\",\"partida_id\":\"" + partidaId + "\"}";
        httpRequest.setContent(jsonData);

        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                if (httpResponse.getStatus().getStatusCode() == HttpStatus.SC_OK) {
                    String responseData = httpResponse.getResultAsString();
                    listener.exito(responseData);
                } else {
                    listener.error(new Throwable("Error al actualizar el inventario. Código de estado HTTP: " + httpResponse.getStatus().getStatusCode()));
                }
            }

            @Override
            public void failed(Throwable t) {
                listener.error(t);
            }

            @Override
            public void cancelled() {
            }
        });
    }

    public static void obtenerPartidas(int jugadorId, final ApiListenerL<List<Partida>> listener) {
        Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.GET);

        httpRequest.setUrl("http://localhost:8080/partida/jugador/" +jugadorId);
        httpRequest.setHeader("Content-Type", "application/json");
        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                HttpStatus status = httpResponse.getStatus();
                if (status.getStatusCode() == HttpStatus.SC_OK) {
                    String jsonResponse = httpResponse.getResultAsString();
                    List<Partida> partidas = parsePartidas(jsonResponse);
                    listener.exito(partidas);
                } else {
                    listener.error(new Throwable("Error al obtener las partidas. Código de estado HTTP: " + status.getStatusCode()));
                }
            }

            @Override
            public void failed(Throwable t) {
                listener.error(t);
            }

            @Override
            public void cancelled() {
                // Handle cancellation if necessary
            }
        });
    }

    private static List<Partida> parsePartidas(String jsonResponse) {
        List<Partida> partidas = new ArrayList<>();
        JsonReader jsonReader = new JsonReader();
        JsonValue root = jsonReader.parse(jsonResponse);

        for (JsonValue partidaValue : root) {
            int id = partidaValue.getInt("id");
            int enemigosAsesinados = partidaValue.getInt("enemigosAsesinados");

            Jugador jugador = new Jugador();
            Inventario inventario = new Inventario();

            Partida partida = new Partida(id, jugador, inventario, enemigosAsesinados);
            partidas.add(partida);
        }

        return partidas;
    }

    public static void obtenerJugadores(final ApiListenerL<List<Jugador>> listener) {
        Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.GET);

        httpRequest.setUrl("http://localhost:8080/jugador/all");
        httpRequest.setHeader("Content-Type", "application/json");
        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                HttpStatus status = httpResponse.getStatus();
                if (status.getStatusCode() == HttpStatus.SC_OK) {
                    String jsonResponse = httpResponse.getResultAsString();
                    List<Jugador> jugadores = parseJugadores(jsonResponse);
                    listener.exito(jugadores);
                } else {
                    listener.error(new Throwable("Error al obtener las partidas. Código de estado HTTP: " + status.getStatusCode()));
                }
            }

            @Override
            public void failed(Throwable t) {
                listener.error(t);
            }

            @Override
            public void cancelled() {
                // Handle cancellation if necessary
            }
        });
    }

    private static List<Jugador> parseJugadores(String jsonResponse) {
        List<Jugador> jugadores = new ArrayList<>();
        JsonReader jsonReader = new JsonReader();
        JsonValue root = jsonReader.parse(jsonResponse);

        for (JsonValue jugadorValue : root) {
            int id = jugadorValue.getInt("id");
            String usuario = jugadorValue.getString("usuario");
            String correo = jugadorValue.getString("correo");


            Jugador j1 = new Jugador();
            j1.setId(id);
            j1.setUsuario(usuario);
            j1.setCorreo(correo);
            jugadores.add(j1);
        }

        return jugadores;
    }

    public static void borrarJugador(int jugadorId, final ApiListener listener) {
        Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.DELETE);
        httpRequest.setUrl("http://localhost:8080/jugador/borrar/"+jugadorId);
        httpRequest.setHeader("Content-Type", "application/json");

        String jsonData = "{\"jugador_id\":\"" + jugadorId + "\"}";
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

    public static void borrarObjeto(int inventario_Id, final ApiListener listener) {
        Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.POST);
        httpRequest.setUrl("http://localhost:8080/jugador/borrar/"+inventario_Id);
        httpRequest.setHeader("Content-Type", "application/json");

        String jsonData = "{\"inventario_Id\":\"" + inventario_Id + "\"}";
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

