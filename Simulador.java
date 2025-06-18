import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Simulador {
    private final EstacaoTransferencia estacao;
    private final Lista<CaminhaoPequeno> caminhoesPequenos;
    private final Lista<CaminhaoGrande> caminhoesGrandes;
    private int minutoAtual;
    private final Zona zonaNorte;
    private final Zona zonaSul;
    private final Zona zonaLeste;
    private final Zona zonaSudeste;
    private final Zona zonaCentro;
    private final Aterro aterro;

    public Simulador() {

        this.aterro = new Aterro();

        // 1. Inicializa a estação com capacidade para 200 toneladas
        this.estacao = new EstacaoTransferencia(200);

        // 2. Inicializa as listas de caminhões
        this.caminhoesPequenos = new Lista<>();
        this.caminhoesGrandes = new Lista<>();

        // 3. Configura as zonas de coleta
        this.zonaNorte = new Zona("Norte",200);
        this.zonaSul = new Zona("Sul",150);
        this.zonaLeste = new Zona("Leste",100);
        this.zonaSudeste = new Zona("Sudeste",160);
        this.zonaCentro = new Zona("Centro",170);

        // 4. Adiciona caminhões pequenos (1 para cada zona)
        adicionarCaminhoesPequenos(zonaNorte, zonaSul, zonaLeste, zonaSudeste, zonaCentro);

        // 5. Adiciona caminhões grandes (2 inicialmente)
        caminhoesGrandes.adicionar(new CaminhaoGrande(1, estacao, aterro));
        caminhoesGrandes.adicionar(new CaminhaoGrande(2, estacao, aterro));
        caminhoesGrandes.adicionar(new CaminhaoGrande(3, estacao, aterro));
        caminhoesGrandes.adicionar(new CaminhaoGrande(4, estacao, aterro));
    }

    private void adicionarCaminhoesPequenos(Zona... zonas) {
        int[] capacidades = {2, 4, 8, 10}; // Capacidades disponíveis
        int idBase = 1;

        for (Zona zona : zonas) {
            int capacidade = capacidades[(int)(Math.random() * capacidades.length)];
            caminhoesPequenos.adicionar(
                    new CaminhaoPequeno(idBase++, capacidade, zona, estacao)
            );
        }
    }

    public void iniciarSimulacao() {
        System.out.println("============ INÍCIO DA SIMULAÇÃO (08:00) ============");
        RelogioSimulado.reiniciar(); // Garante início limpo

        ExecutorService executor = Executors.newFixedThreadPool(
                caminhoesPequenos.tamanho() + caminhoesGrandes.tamanho()
        );

        // Cria threads para caminhões pequenos
        for (int i = 0; i < caminhoesPequenos.tamanho(); i++) {
            CaminhaoPequeno caminhao = caminhoesPequenos.obter(i);
            executor.execute(() -> {
                while (RelogioSimulado.getMinutosTotais() < RelogioSimulado.MINUTOS_MAXIMOS) {
                    try {
                        caminhao.executarCiclo();
                        Thread.sleep((10 + (int)(Math.random() * 10)) * 10L); // Simula tempo real
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            });
        }

        // Cria threads para caminhões grandes
        for (int i = 0; i < caminhoesGrandes.tamanho(); i++) {
            CaminhaoGrande caminhao = caminhoesGrandes.obter(i);
            executor.execute(() -> {
                while (RelogioSimulado.getMinutosTotais() < RelogioSimulado.MINUTOS_MAXIMOS) {
                    try {
                        caminhao.executarCiclo();
                        Thread.sleep((30 + (int)(Math.random() * 20)) * 10L); // Simula tempo real
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            });
        }

        // Avança o tempo global de forma centralizada
        new Thread(() -> {
            while (RelogioSimulado.avancarTempo(1)) {
                try {
                    Thread.sleep(100); // 100ms por minuto simulado (~72s para o dia todo)
                } catch (InterruptedException e) {
                    break;
                }
            }
        }).start();

        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.MINUTES); // Tempo real máximo da simulação

            int lixoTotalRestante = zonaNorte.getLixoRestante()
                    + zonaSul.getLixoRestante()
                    + zonaLeste.getLixoRestante()
                    + zonaSudeste.getLixoRestante()
                    + zonaCentro.getLixoRestante();

            EstatisticasSimulacao.setLixoRestanteNasZonas(lixoTotalRestante);


        } catch (InterruptedException e) {
            executor.shutdownNow();
        }

        EstatisticasSimulacao.adicionarLixoDescarregado(aterro.getLixoRecebido());

        System.out.println("============ FIM DA SIMULAÇÃO (20:00) ============");
        //System.out.println("Total de lixo recebido no aterro: " + aterro.getLixoRecebido() + " toneladas");

        EstatisticasSimulacao.exibirResumoFinal();


    }


    private String formatarHora(int minuto) {
        int hora = 8 + (minuto / 60);
        int minutos = minuto % 60;
        return String.format("%02d:%02d", hora, minutos);
    }

    private String getHoraSimulada(int minutos) {
        if (minutos < 0 || minutos >= 720) {
            throw new IllegalArgumentException("Minutos devem estar entre 0 e 719");
        }

        int hora = 8 + (minutos / 60);
        int minuto = minutos % 60;

        return String.format("%02d:%02d", hora, minuto);
    }
}