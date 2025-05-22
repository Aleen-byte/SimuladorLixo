public class CaminhaoGrande {
    private final int id;
    private final EstacaoTransferencia estacao;
    private final int capacidade = 20;
    private boolean emViagem;

    public CaminhaoGrande(int id, EstacaoTransferencia estacao) {
        this.id = id;
        this.estacao = estacao;
    }

    public void executarCiclo(int minutoAtual) throws InterruptedException {
        // Verificar se há lixo suficiente
        if (estacao.getLixoAcumulado() >= capacidade) {
            emViagem = true;

            // Carregar lixo
            System.out.println(formatarHora(minutoAtual) + " - Caminhão Grande " + id + " começando carregamento");
            int tempoCarregar = 4 + (int)(Math.random() * 3);
            //Thread.sleep(tempoCarregar * 1000);

            int lixoCarregado = estacao.fornecerLixo(capacidade);
            System.out.println(formatarHora(minutoAtual + tempoCarregar) + " - Caminhão Grande " + id +
                    " carregou " + lixoCarregado + " toneladas");

            // Ir para o aterro
            System.out.println(formatarHora(minutoAtual + tempoCarregar) + " - Caminhão Grande " + id + " indo para o aterro");
            int tempoViagem = 10 + (int)(Math.random() * 6);
            //Thread.sleep(tempoViagem * 1000);

            // Descarregar no aterro
            System.out.println(formatarHora(minutoAtual + tempoCarregar + tempoViagem) + " - Caminhão Grande " + id +
                    " chegou no aterro");
            int tempoDescarregar = 3 + (int)(Math.random() * 4);
            //Thread.sleep(tempoDescarregar * 1000);

            System.out.println(formatarHora(minutoAtual + tempoCarregar + tempoViagem + tempoDescarregar) +
                    " - Caminhão Grande " + id + " descarregou no aterro");

            // Voltar para estação
            System.out.println(formatarHora(minutoAtual + tempoCarregar + tempoViagem + tempoDescarregar) +
                    " - Caminhão Grande " + id + " voltando para estação");
            //Thread.sleep(tempoViagem * 1000);

            emViagem = false;
        } else {
            // Esperar até ter lixo suficiente
            //Thread.sleep(5000);
        }
    }

    private String formatarHora(int minuto) {
        int hora = 8 + (minuto / 60);
        int min = minuto % 60;
        return String.format("%02d:%02d", hora, min);
    }
}