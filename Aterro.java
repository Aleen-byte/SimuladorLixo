public class Aterro {
    private int lixoRecebido;
    private final Object lock = new Object();

    public Aterro() {
        this.lixoRecebido = 0;
    }

    // Método para registrar o lixo descarregado
    public void registrarDescarregamento(int quantidade) {
        synchronized (lock) {
            lixoRecebido += quantidade;
        }
    }

    public int getLixoRecebido() {
        synchronized (lock) {
            return lixoRecebido;
        }
    }
}
