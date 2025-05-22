public class EstacaoTransferencia {
    private final Fila<CaminhaoPequeno> filaDescarregamento;
    private int lixoAcumulado;
    private final int capacidadeMaxima;
    private boolean emDescarregamento;
    private final Object lock = new Object();

    public EstacaoTransferencia(int capacidadeMaxima) {
        this.filaDescarregamento = new Fila<>(20); // Capacidade para 20 caminhões
        this.capacidadeMaxima = capacidadeMaxima;
        iniciarProcessamentoFila();
    }

    private void iniciarProcessamentoFila() {
        new Thread(() -> {
            while (true) {
                try {
                    processarProximoCaminhao();
                    //Thread.sleep(100); // Intervalo para verificar a fila
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }).start();
    }

    private void processarProximoCaminhao() throws InterruptedException {
        synchronized (lock) {
            if (!filaDescarregamento.estaVazia() && !emDescarregamento) {
                emDescarregamento = true;
                CaminhaoPequeno caminhao = filaDescarregamento.desenfileirar();

                // Tempo de descarregamento (2-5 segundos)
                int tempoDescarregamento = 2000 + (int)(Math.random() * 3000);
                //Thread.sleep(tempoDescarregamento);

                receberLixo(caminhao.getCargaAtual());
                caminhao.finalizarDescarregamento();

                System.out.println("[" + caminhao.getHoraSimulada() + "] Caminhão " + caminhao.getIdCaminhao() +
                        " descarregou " + caminhao.getCargaAtual() + " toneladas. " +
                        "Fila: " + filaDescarregamento.tamanho());

                emDescarregamento = false;
                lock.notifyAll();
            }
        }
    }

    public void adicionarNaFila(CaminhaoPequeno caminhao) {
        synchronized (lock) {
            filaDescarregamento.enfileirar(caminhao);
            System.out.println("[" + caminhao.getHoraSimulada() + "] Caminhão " + caminhao.getIdCaminhao() +
                    " entrou na fila (Tamanho: " + filaDescarregamento.tamanho() + ")");
        }
    }

    public void processarFila(int minutoAtual) throws InterruptedException {
        if (!filaDescarregamento.estaVazia() && !emDescarregamento) {
            synchronized (this) {
                emDescarregamento = true;
                CaminhaoPequeno caminhao = filaDescarregamento.desenfileirar();
                System.out.println(formatarHora(minutoAtual) + " - Caminhão Pequeno " + caminhao.getIdCaminhao() +
                        " começando descarregamento");
                caminhao.descarregar(minutoAtual);
                emDescarregamento = false;
            }
        }
    }

    public synchronized void receberLixo(int quantidade) {
        lixoAcumulado += quantidade;
        if (lixoAcumulado > capacidadeMaxima) {
            lixoAcumulado = capacidadeMaxima;
        }
    }

    public synchronized int fornecerLixo(int quantidade) {
        int lixoFornecido = Math.min(lixoAcumulado, quantidade);
        lixoAcumulado -= lixoFornecido;
        return lixoFornecido;
    }

    public synchronized int getLixoAcumulado() {
        return lixoAcumulado;
    }

    private String formatarHora(int minuto) {
        int hora = 8 + (minuto / 60);
        int min = minuto % 60;
        return String.format("%02d:%02d", hora, min);
    }
}