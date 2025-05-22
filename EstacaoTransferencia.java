public class EstacaoTransferencia {
    private final Fila<CaminhaoPequeno> filaDescarregamento;
    private int lixoAcumulado;
    private final int capacidadeMaxima;
    private final Object lock = new Object();
    private boolean emDescarregamento = false;

    public EstacaoTransferencia(int capacidadeMaxima) {
        this.filaDescarregamento = new Fila<>(20); // até 20 caminhões
        this.capacidadeMaxima = capacidadeMaxima;
        iniciarProcessamentoFila();
    }

    private void iniciarProcessamentoFila() {
        Thread processadorFila = new Thread(() -> {
            while (true) {
                try {
                    processarProximoCaminhao();
                    Thread.sleep(100); // verificação periódica
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        processadorFila.setDaemon(true); // encerra com a aplicação
        processadorFila.start();
    }

    private void processarProximoCaminhao() throws InterruptedException {
        CaminhaoPequeno caminhao = null;

        synchronized (lock) {
            if (!filaDescarregamento.estaVazia() && !emDescarregamento) {
                emDescarregamento = true;
                caminhao = filaDescarregamento.desenfileirar();
            }
        }

        if (caminhao != null) {
            // Tempo de descarregamento (2-5 minutos)
            int tempoDescarregar = 2 + (int)(Math.random() * 4);
            Thread.sleep(tempoDescarregar * 100); // reduzir para testes (tempo real seria *1000)
            RelogioSimulado.avancarTempo(tempoDescarregar);

            receberLixo(caminhao.getCargaAtual());

            System.out.println("[" + caminhao.getHoraSimulada() + "] Caminhão " + caminhao.getIdCaminhao() +
                    " descarregou " + caminhao.getCargaAtual() + " toneladas. Caminhões restantes na fila: " +
                    filaDescarregamento.tamanho());

            caminhao.finalizarDescarregamento();


            synchronized (lock) {
                emDescarregamento = false;
                lock.notifyAll(); // acorda outros que estiverem esperando
            }
        }
    }

    public void adicionarNaFila(CaminhaoPequeno caminhao) {
        synchronized (lock) {
            filaDescarregamento.enfileirar(caminhao);
            System.out.println("[" + caminhao.getHoraSimulada() + "] Caminhão " + caminhao.getIdCaminhao() +
                    " entrou na fila (tamanho atual: " + filaDescarregamento.tamanho() + ")");
        }
    }

    public synchronized void receberLixo(int quantidade) {
        lixoAcumulado += quantidade;
        if (lixoAcumulado > capacidadeMaxima) {
            lixoAcumulado = capacidadeMaxima;
        }
    }

    public synchronized int fornecerLixo(int quantidade) {
        int fornecido = Math.min(lixoAcumulado, quantidade);
        lixoAcumulado -= fornecido;
        return fornecido;
    }

    public synchronized int getLixoAcumulado() {
        return lixoAcumulado;
    }
}
