package model;

public class Inventario {
    private int id;
    private Partida partida;
    private Objeto objeto;

    public Inventario() {
    }

    public Inventario(int id, Partida partida, Objeto objeto) {
        this.id = id;
        this.partida = partida;
        this.objeto = objeto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Partida getPartida() {
        return partida;
    }

    public void setPartida(Partida partida) {
        this.partida = partida;
    }

    public Objeto getObjeto() {
        return objeto;
    }

    public void setObjeto(Objeto objeto) {
        this.objeto = objeto;
    }
}
