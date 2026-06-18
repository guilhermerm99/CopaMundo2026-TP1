package copamundo.selecoes.persistencia;

// Contrato que toda entidade persistível deve cumprir: ter um identificador único
public interface Identificavel {
    String getId();
}
