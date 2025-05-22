public class FilaCaminhoes {
    private CaminhaoPequeno[] caminhoes;
    private int inicio;
    private int fim;
    private int tamanho;
    private final int capacidade;

    public FilaCaminhoes(int capacidade) {
        this.capacidade = capacidade;
        this.caminhoes = new CaminhaoPequeno[capacidade];
        this.inicio = 0;
        this.fim = -1;
        this.tamanho = 0;
    }

    public synchronized void enfileirar(CaminhaoPequeno caminhao) {
        if (tamanho == capacidade) {
            throw new IllegalStateException("Fila cheia");
        }
        fim = (fim + 1) % capacidade;
        caminhoes[fim] = caminhao;
        tamanho++;
    }

    public synchronized CaminhaoPequeno desenfileirar() {
        if (tamanho == 0) {
            return null;
        }
        CaminhaoPequeno caminhao = caminhoes[inicio];
        inicio = (inicio + 1) % capacidade;
        tamanho--;
        return caminhao;
    }

    public synchronized boolean estaVazia() {
        return tamanho == 0;
    }

    public synchronized int tamanho() {
        return tamanho;
    }
}