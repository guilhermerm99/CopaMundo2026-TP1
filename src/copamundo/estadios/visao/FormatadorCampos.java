package copamundo.estadios.visao;

final class FormatadorCampos {
    private FormatadorCampos() {
    }

    static int lerCapacidade(String texto) {
        if (texto == null || texto.isBlank()) {
            throw new NumberFormatException("Capacidade vazia.");
        }

        String normalizado = texto.trim().replace(".", "");
        if (!normalizado.matches("\\d+")) {
            throw new NumberFormatException("Capacidade invalida.");
        }

        return Integer.parseInt(normalizado);
    }
}
