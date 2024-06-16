package api;

public class PartidaInfo {
    private int partidaId;
    private int inventarioId;
    private int enemigosAsesinados;

    public PartidaInfo(int partidaId, int inventarioId,int enemigosAsesinados) {
        this.partidaId = partidaId;
        this.inventarioId = inventarioId;
        this.enemigosAsesinados=enemigosAsesinados;
    }

    public int getPartidaId() {
        return partidaId;
    }

    public int getInventarioId() {
        return inventarioId;
    }

    public int getEnemigosAsesinados() {
        return enemigosAsesinados;
    }

    public void setEnemigosAsesinados(int enemigosAsesinados) {
        this.enemigosAsesinados = enemigosAsesinados;
    }
}
