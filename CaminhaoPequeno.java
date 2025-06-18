public class CaminhaoPequeno {
    private final int id;
    private final int capacidade;
    private final Zona zona;
    private final EstacaoTransferencia estacao;
    private int cargaAtual;
    private boolean emViagem = true; // começa como true para entrar no wait
    private int minutosAcumulados = 0;

    public CaminhaoPequeno(int id, int capacidade, Zona zona, EstacaoTransferencia estacao) {
        this.id = id;
        this.capacidade = capacidade;
        this.zona = zona;
        this.estacao = estacao;
    }

    private int tempoDescarregar = 2 + (int)(Math.random() * 4);

    public int getIdCaminhao() {
        return id;
    }

    public String getHoraSimulada() {
        return RelogioSimulado.getHoraSimulada();
    }

    public void executarCiclo() throws InterruptedException {
        EstatisticasSimulacao.registrarCaminhaoPequeno();
        // 1. Coletar lixo
        System.out.println("[" + getHoraSimulada() + "] Caminhão " + id + " chegou na zona " + zona.getNome());
        cargaAtual = zona.coletarLixo(capacidade);

        int tempoColeta = 4 + (int)(Math.random() * 3);
        Thread.sleep(tempoColeta * 100); // Simulação em tempo real
        RelogioSimulado.avancarTempo(tempoColeta);
        minutosAcumulados += tempoColeta;
        System.out.println("[" + getHoraSimulada() + "] Caminhão " + id + " coletou " + cargaAtual + " toneladas");

        // 2. Viagem até a estação
        int tempoViagem = 5 + (int)(Math.random() * 3);
        Thread.sleep(tempoViagem * 100);
        RelogioSimulado.avancarTempo(tempoViagem);
        minutosAcumulados += tempoViagem;
        System.out.println("[" + getHoraSimulada() + "] Caminhão " + id + " chegou na estação");

        // 3. Fila de descarregamento
        emViagem = true;
        estacao.adicionarNaFila(this);

        synchronized (this) {
            while (emViagem) {
                wait();
            }
        }

        // 4. Retorno à zona
        int tempoRetorno = 5 + (int)(Math.random() * 3);
        Thread.sleep(tempoRetorno * 100);
        RelogioSimulado.avancarTempo(tempoRetorno);
        minutosAcumulados += tempoRetorno;
    }

    public void finalizarDescarregamento() {
        synchronized (this) {
            emViagem = false;
            notify();
        }
    }

    public int getCargaAtual() {
        return cargaAtual;
    }

}
