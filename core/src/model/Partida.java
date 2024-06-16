package model;

public class Partida {
    private int id;
    private Jugador jugador;
    private Inventario inventario;
    private int enemigosAsesinados;

    public Partida() {
    }

    public Partida(int id, Jugador jugador, Inventario inventario, int enemigosAsesinados) {
        this.id = id;
        this.jugador = jugador;
        this.inventario = inventario;
        this.enemigosAsesinados = enemigosAsesinados;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public Inventario getInventario() {
        return inventario;
    }

    public void setInventario(Inventario inventario) {
        this.inventario = inventario;
    }

    public int getEnemigosAsesinados() {
        return enemigosAsesinados;
    }

    public void setEnemigosAsesinados(int enemigosAsesinados) {
        this.enemigosAsesinados = enemigosAsesinados;
    }
}
