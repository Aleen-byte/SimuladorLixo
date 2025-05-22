public class Zona {
    private final String nome;
    private int lixoDisponivel; // Lixo atual na zona

    public Zona(String nome, int lixoInicial) {
        this.nome = nome;
        this.lixoDisponivel = lixoInicial;
    }

    public synchronized int coletarLixo(int capacidade) {
        int coletado = Math.min(lixoDisponivel, capacidade);
        lixoDisponivel -= coletado;
        return coletado;
    }

    public synchronized int getLixoRestante() {
        return lixoDisponivel;
    }

    public String getNome() {
        return nome;
    }

    // Para debug ou estatísticas
    @Override
    public String toString() {
        return "Zona " + nome + " (Lixo restante: " + lixoDisponivel + " toneladas)";
    }
}
