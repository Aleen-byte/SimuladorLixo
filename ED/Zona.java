public class Zona {
    private final String nome;
    private final int lixoMin;
    private final int lixoMax;

    public Zona(String nome, int lixoMin, int lixoMax) {
        this.nome = nome;
        this.lixoMin = lixoMin;
        this.lixoMax = lixoMax;
    }

    public int coletarLixo(int capacidadeCaminhao) {
        int lixoGerado = lixoMin + (int)(Math.random() * (lixoMax - lixoMin + 1));
        return Math.min(lixoGerado, capacidadeCaminhao);
    }

    public String getNome() {
        return nome;
    }
}