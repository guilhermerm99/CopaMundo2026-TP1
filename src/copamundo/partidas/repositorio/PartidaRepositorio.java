package copamundo.partidas.repositorio;

import copamundo.comum.Partida;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class PartidaRepositorio {

    private static final Path PASTA_DADOS = Paths.get("dados");

    private static final Path ARQUIVO_PARTIDAS = Paths.get("dados", "partidas.dat");


    public static void salvarListaPartidas(List<Partida> partidas) throws IOException {
        Files.createDirectories(PASTA_DADOS);

        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(ARQUIVO_PARTIDAS))) {
            out.writeObject(partidas);
        }
    }


    public static List<Partida> carregarListaPartidas() throws IOException, ClassNotFoundException {

        if (!Files.exists(ARQUIVO_PARTIDAS)) {
            return new ArrayList<>();
        }

        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(ARQUIVO_PARTIDAS))) {
            return converterListaPartidas(in.readObject());
        }
    }

    private static List<Partida> converterListaPartidas(Object objeto) throws IOException {
        if (!(objeto instanceof List<?> itens)) {
            throw new IOException("Arquivo de partidas possui formato invalido.");
        }

        List<Partida> partidas = new ArrayList<>();
        for (Object item : itens) {
            if (!(item instanceof Partida partida)) {
                throw new IOException("Arquivo de partidas contem item invalido.");
            }
            partidas.add(partida);
        }
        return partidas;
    }
}
