public class CaminhaoPequeno {
    private final int id;
    private final int capacidade;
    private final Zona zona;
    private final EstacaoTransferencia estacao;
    private int cargaAtual;
    private boolean emViagem;
    private int minutosAcumulados;

    public CaminhaoPequeno(int id, int capacidade, Zona zona, EstacaoTransferencia estacao) {
        this.id = id;
        this.capacidade = capacidade;
        this.zona = zona;
        this.estacao = estacao;
    }



    public void descarregar(int minutoAtual) throws InterruptedException {
        // Tempo de descarregamento (2-5 minutos)
        int tempoDescarregar = 2 + (int)(Math.random() * 4);
        //Thread.sleep(tempoDescarregar * 1000);

        estacao.receberLixo(cargaAtual);
        System.out.println(formatarHora(minutoAtual + tempoDescarregar) + " - Caminhão Pequeno " + id +
                " descarregou " + cargaAtual + " toneladas na estação");
        cargaAtual = 0;

        synchronized (this) {
            emViagem = false;
            notify();
        }
    }

    public int getIdCaminhao() {
        return id;
    }

    private String formatarHora(int minuto) {
        int hora = 8 + (minuto / 60);
        int min = minuto % 60;
        return String.format("%02d:%02d", hora, min);
    }

    public String getHoraSimulada() {
        int horaBase = 8; // Início do turno às 8h
        int horaTotal = horaBase + (minutosAcumulados / 60);
        int minutos = minutosAcumulados % 60;

        // Garante que não passe das 20h
        horaTotal = horaTotal > 20 ? 20 : horaTotal;

        return String.format("%02d:%02d", horaTotal, minutos);
    }

    public void executarCiclo(int minutoAtual) throws InterruptedException {
        // 1. Coletar lixo na zona
        System.out.println("[" + getHoraSimulada() + "] Caminhão " + id + " chegou na zona " + zona.getNome());
        cargaAtual = zona.coletarLixo(capacidade);

        // Tempo de coleta (2-5 minutos)
        int tempoColeta = 2 + (int)(Math.random() * 4);
        //Thread.sleep(tempoColeta * 1000);
        minutosAcumulados += tempoColeta;

        System.out.println("[" + getHoraSimulada() + "] Caminhão " + id + " coletou " + cargaAtual + " toneladas");

        // 2. Ir para estação
        int tempoViagem = 3 + (int)(Math.random() * 3); // 3-6 minutos
        //Thread.sleep(tempoViagem * 1000);
        minutosAcumulados += tempoViagem;

        // 3. Entrar na fila de descarregamento
        System.out.println("[" + getHoraSimulada() + "] Caminhão " + id + " chegou na estação");
        estacao.adicionarNaFila(this);

        // 4. Esperar descarregamento
        synchronized (this) {
            while (emViagem) {
                wait();
            }
        }

        // 5. Tempo de retorno à zona
        int tempoRetorno = 2 + (int)(Math.random() * 3); // 2-5 minutos
        //Thread.sleep(tempoRetorno * 1000);
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