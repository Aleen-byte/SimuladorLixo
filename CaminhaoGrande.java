public class CaminhaoGrande {
    private final int id;
    private final EstacaoTransferencia estacao;
    private final int capacidade = 20;
    private boolean emViagem;
    private int minutosAcumuladosCG;
    private final Aterro aterro;

    public CaminhaoGrande(int id, EstacaoTransferencia estacao, Aterro aterro) {
        this.id = id;
        this.estacao = estacao;
        this.minutosAcumuladosCG = 0;
        this.aterro = aterro;
    }

    public String getHoraSimulada() {
        return RelogioSimulado.getHoraSimulada();
    }

    public void executarCiclo() throws InterruptedException {
        if (estacao.getLixoAcumulado() >= capacidade) {
            emViagem = true;

            EstatisticasSimulacao.registrarCaminhaoGrande();



            // 1. Carregar lixo
            System.out.println("[" + getHoraSimulada() + "] - Caminhão Grande " + id + " começando carregamento");
            int tempoCarregar = 4 + (int)(Math.random() * 3); // 4-6 minutos
            Thread.sleep(tempoCarregar * 100);
            RelogioSimulado.avancarTempo(tempoCarregar);
            minutosAcumuladosCG += tempoCarregar;

            int lixoCarregado = estacao.fornecerLixo(capacidade);
            System.out.println("[" + getHoraSimulada() + "] - Caminhão Grande " + id +
                    " carregou " + lixoCarregado + " toneladas");

            // 2. Ir para o aterro
            System.out.println("[" + getHoraSimulada() + "] - Caminhão Grande " + id + " indo para o aterro");
            int tempoViagem = 10 + (int)(Math.random() * 6); // 10-15 minutos
            Thread.sleep(tempoViagem * 100);
            RelogioSimulado.avancarTempo(tempoViagem);
            minutosAcumuladosCG += tempoViagem;

            // 3. Descarregar no aterro
            System.out.println("[" + getHoraSimulada() + "] - Caminhão Grande " + id + " chegou no aterro");
            int tempoDescarregar = 3 + (int)(Math.random() * 4); // 3-6 minutos
            Thread.sleep(tempoDescarregar * 100);
            RelogioSimulado.avancarTempo(tempoDescarregar);
            minutosAcumuladosCG += tempoDescarregar;



            System.out.println("[" + getHoraSimulada() + "] - Caminhão Grande " + id +
                    " descarregou " + lixoCarregado + " toneladas no aterro");
            aterro.registrarDescarregamento(lixoCarregado);

            // 4. Voltar para estação
            System.out.println("[" + getHoraSimulada() + "] - Caminhão Grande " + id + " voltando para estação");
            int tempoVolta = 10 + (int)(Math.random() * 6); // 10-15 minutos
            Thread.sleep(tempoVolta * 100);
            RelogioSimulado.avancarTempo(tempoVolta);
            minutosAcumuladosCG += tempoVolta;

            emViagem = false;
        } else {
            // Se não há lixo suficiente, espera um pouco (simulado)
            System.out.println("[" + getHoraSimulada() + "] - Caminhão Grande " + id +
                    " aguardando lixo suficiente na estação");
            Thread.sleep(5000); // Espera 0.5s = 0.5 min (simulado)
            RelogioSimulado.avancarTempo(1); // Opcional: pode ou não contar como 1 minuto
        }
    }

    public int getMinutosAcumulados() {
        return minutosAcumuladosCG;
    }

    public int getId() {
        return id;
    }
}
