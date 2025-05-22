import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Simulador {
    private final EstacaoTransferencia estacao;
    private final Lista<CaminhaoPequeno> caminhoesPequenos;
    private final Lista<CaminhaoGrande> caminhoesGrandes;
    private final int duracaoJornada = 720; // 12 horas (de 8h às 20h)

    public Simulador() {
        // 1. Inicializa a estação com capacidade para 200 toneladas
        this.estacao = new EstacaoTransferencia(200);

        // 2. Inicializa as listas de caminhões
        this.caminhoesPequenos = new Lista<>();
        this.caminhoesGrandes = new Lista<>();

        // 3. Configura as zonas de coleta
        Zona zonaNorte = new Zona("Norte", 5, 15);
        Zona zonaSul = new Zona("Sul", 5, 16);
        Zona zonaLeste = new Zona("Leste", 5, 14);
        Zona zonaOeste = new Zona("Oeste", 5, 15);

        // 4. Adiciona caminhões pequenos (1 para cada zona)
        adicionarCaminhoesPequenos(zonaNorte, zonaSul, zonaLeste, zonaOeste);

        // 5. Adiciona caminhões grandes (2 inicialmente)
        caminhoesGrandes.adicionar(new CaminhaoGrande(1, estacao));
        caminhoesGrandes.adicionar(new CaminhaoGrande(2, estacao));
    }

    private void adicionarCaminhoesPequenos(Zona... zonas) {
        int[] capacidades = {4, 8, 10}; // Capacidades disponíveis
        int idBase = 1;

        for (Zona zona : zonas) {
            int capacidade = capacidades[(int)(Math.random() * capacidades.length)];
            caminhoesPequenos.adicionar(
                    new CaminhaoPequeno(idBase++, capacidade, zona, estacao)
            );
        }
    }

    public void iniciarSimulacao() {
        System.out.println("==== INÍCIO DA SIMULAÇÃO (08:00) ====");

        ExecutorService executor = Executors.newFixedThreadPool(
                caminhoesPequenos.tamanho() + caminhoesGrandes.tamanho()
        );

        // Inicia os caminhões pequenos
        for (int i = 0; i < caminhoesPequenos.tamanho(); i++) {
            CaminhaoPequeno caminhao = caminhoesPequenos.obter(i);
            executor.execute(() -> {
                int minutoAtual = 0;
                while (minutoAtual < duracaoJornada) {
                    try {
                        caminhao.executarCiclo(minutoAtual);
                        minutoAtual += 10 + (int)(Math.random() * 10); // 10-20 minutos entre ciclos
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            });
        }

        // Inicia os caminhões grandes
        for (int i = 0; i < caminhoesGrandes.tamanho(); i++) {
            CaminhaoGrande caminhao = caminhoesGrandes.obter(i);
            executor.execute(() -> {
                int minutoAtual = 0;
                while (minutoAtual < duracaoJornada) {
                    try {
                        caminhao.executarCiclo(minutoAtual);
                        minutoAtual += 30 + (int)(Math.random() * 20); // 30-50 minutos entre ciclos
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(duracaoJornada, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }

        System.out.println("==== FIM DA SIMULAÇÃO (20:00) ====");
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