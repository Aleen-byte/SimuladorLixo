public class EstatisticasSimulacao {
    private static int caminhoesPequenosUsados = 0;
    private static int caminhoesGrandesUsados = 0;
    private static int lixoDescarregadoNoAterro = 0;
    private static int lixoRestanteNasZonas = 0;

    public static synchronized void registrarCaminhaoPequeno() {
        caminhoesPequenosUsados++;
    }

    public static synchronized void registrarCaminhaoGrande() {
        caminhoesGrandesUsados++;
    }

    public static synchronized void adicionarLixoDescarregado(int quantidade) {
        lixoDescarregadoNoAterro += quantidade;
    }

    public static synchronized void setLixoRestanteNasZonas(int quantidade) {
        lixoRestanteNasZonas = quantidade;
    }

    public static synchronized void exibirResumoFinal() {
        System.out.println("\n=========== RESUMO FINAL DA SIMULAÇÃO ============");
        System.out.println("Caminhões Pequenos Foram Usados Usados: " + caminhoesPequenosUsados + " Vezes");
        System.out.println("Caminhões Grandes Foram Usados Usados: " + caminhoesGrandesUsados + " Vezes");
        System.out.println("Lixo Descarregado no Aterro: " + lixoDescarregadoNoAterro + " toneladas");
        System.out.println("Lixo Restante nas Zonas: " + lixoRestanteNasZonas + " toneladas");
        System.out.println("==================================================\n");
    }
}
