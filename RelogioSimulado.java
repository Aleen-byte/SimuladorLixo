public class RelogioSimulado {
    private static int minutosTotais = 0;
    static final int MINUTOS_MAXIMOS = 720; // 12 horas (8h às 20h)

    /**
     * Avança o tempo simulado, garantindo que não ultrapasse 20h
     * @param minutos minutos a avançar
     * @return true se o tempo foi avançado, false se já atingiu o limite
     */
    public static synchronized boolean avancarTempo(int minutos) {
        if (minutosTotais >= MINUTOS_MAXIMOS) {
            return false;
        }

        minutosTotais = Math.min(minutosTotais + minutos, MINUTOS_MAXIMOS);
        return true;
    }

    /**
     * @return horário simulado no formato HH:MM (08:00 às 20:00)
     */
    public static synchronized String getHoraSimulada() {
        int hora = 8 + (minutosTotais / 60);
        int minuto = minutosTotais % 60;

        // Garantia extra
        if (hora >= 20) {
            hora = 20;
            minuto = 0;
        }

        return String.format("%02d:%02d", hora, minuto);
    }

    /**
     * @return minutos totais decorridos (0 a 720)
     */
    public static synchronized int getMinutosTotais() {
        return minutosTotais;
    }

    /**
     * Reinicia o relógio para o início do turno (08:00)
     */
    public static synchronized void reiniciar() {
        minutosTotais = 0;
    }
}