package copamundo.usuarios.controle;

import copamundo.usuarios.excecoes.PersistenciaException;
import copamundo.usuarios.persistencia.PersistenciaUsuarios;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Usuario implements Serializable {
    private String nomeUsuario;
    private String emailUsuario;
    private String senhaUsuario;

    public enum Status {
        TODOS("Todos os status"),ATIVO("Ativo"), INATIVO("Inativo");

        private final String nomeExibicao;

        Status(String nomeExibicao) {
            this.nomeExibicao = nomeExibicao;
        }

        @Override
        public String toString() {
            return nomeExibicao;
        }
    }

    public enum Funcao {
        TODOS("Todas as funções"),ADMINISTRADOR("Administrador"), ORGANIZADOR("Organizador"), ARBITRO("Árbitro");

        private final String nomeExibicao;

        Funcao(String nomeExibicao) {
            this.nomeExibicao = nomeExibicao;
        }

        @Override
        public String toString() {
            return nomeExibicao;
        }
    }

    public enum Pais {
        TODOS("Todos os países"), AFRICADOSUL("África do Sul"), ALEMANHA("Alemanha"), ARGELIA("Argélia"), ARABIASAUDITA("Arábia Saudita"), ARGENTINA("Argentina"), AUSTRALIA("Austrália"), AUSTRIA("Áustria"),
        BELGICA("Bélgica"), BOSNIAEHERZEGOVINA("Bósnia e Hezergorvina"), BRASIL("Brasil"), CABOVERDE("Cabo Verde"), CANADA("Canadá"), CATAR("Catar"), COLOMBIA("Colômbia"),
        COREIADOSUL("Coréia do Sul"), COSTADOMARFIM("Costa do Marfim"), CROACIA("Croácia"), CURACAO("Curaçao"), ECUADOR("Equador"), EGITO("Egito"), ESCOCIA("Escócia"),
        ESPANHA("Espanha"), ESTADOSUNIDOS("Estados Unidos"), FRANCA("França"), GANA("Gana"), HAITI("Haiti"), HOLANDA("Holanda"), INGLATERRA("Inglaterra"), IRA("Irã"),
        IRAQUE("Iraque"), JAPAO("Japão"), JORDANIA("Jordânia"), MARROCOS("Marrocos"), MEXICO("México"), NORUEGA("Noruega"), NOVAZELANDIA("Nova Zelândia"), PANAMA("Panamá"),
        PARAGUAI("Paraguai"), PORTUGAL("Portugal"), RDDOCONGO("República do Congo"), REPUBLICATCHECA("República Tcheca"), SENEGAL("Senegal"), SUICA("Suíça"), SUECA("Suécia"),
        TUNISIA("Tunísia"), TURQUIA("Turquia"), URUGUAI("Uruguai"), UZBEQUISTAO("Uzbequistão");

        private final String nomeExibicao;

        Pais(String nomeExibicao) {
            this.nomeExibicao = nomeExibicao;
        }

        @Override
        public String toString() {
            return nomeExibicao;
        }
    }

    private Funcao funcao;
    private Status status;
    private Pais pais;
    private String Cpf;
    private Date dataCadastro;
    private Usuario criadoPor;
    private boolean trocaSenhaObrigatoria;

    public static List<Usuario> usuarios = new ArrayList<>();

    static {
        copamundo.usuarios.persistencia.PersistenciaUsuarios persistencia = new copamundo.usuarios.persistencia.PersistenciaUsuarios();
        try {
            // Carrega o arquivo dados/usuarios.dat para a memória
            usuarios = persistencia.carregarUsuarios();
            System.out.println("Usuários carregados do arquivo com sucesso. Total: " + usuarios.size());
        } catch (Exception e) {
            System.out.println("Arquivo não encontrado ou erro ao ler. Iniciando lista vazia.");
            usuarios = new ArrayList<>();
        }

        if (usuarios.isEmpty()) {
            Usuario adminPadrao = new Usuario(
                    "Administrador",
                    "administrador@copa.com", // E-mail para logar
                    "Comum@1234",         // Senha para logar
                    Status.ATIVO,
                    Pais.BRASIL,
                    Funcao.ADMINISTRADOR,
                    "00000000000"
            );

            try {
                persistencia.salvarUsuario(adminPadrao);
                usuarios = persistencia.carregarUsuarios(); // Atualiza a lista na memória
            } catch (Exception e) {
                System.out.println("Erro ao criar admin padrão: " + e.getMessage());
            }
        }
    }

    private static final long serialVersionUID = 1L;

    public Usuario(String nomeUsuario, String emailUsuario, String senhaUsuario, Status status, Pais pais, Funcao funcao, String Cpf) {
        this.nomeUsuario = nomeUsuario;
        this.emailUsuario = emailUsuario;
        this.senhaUsuario = senhaUsuario;
        this.status = status;
        this.pais = pais;
        this.funcao = funcao;
        this.Cpf = Cpf;
        this.dataCadastro = new Date();
    }

    // setters
    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }
    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }
    public void setSenhaUsuario(String senhaUsuario) {
        this.senhaUsuario = senhaUsuario;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public void setFuncao(Funcao funcao) {
        this.funcao = funcao;
    }
    public void setPais(Pais pais) {
        this.pais = pais;
    }
    public void setCpf(String Cpf) {
        this.Cpf = Cpf;
    }
    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
    public void setCriadoPor(Usuario criadoPor) {
        this.criadoPor = criadoPor;
    }

    // getters
    public String getNomeUsuario() {
        return nomeUsuario;
    }
    public String getEmailUsuario() {
        return emailUsuario;
    }
    public Status getStatus() {
        return status;
    }
    public Pais getPais() {
        return pais;
    }
    public Funcao getFuncao() {
        return funcao;
    }
    public String getCpf() {
        return Cpf;
    }
    public Date getDataCadastro() {
        return dataCadastro;
    }
    public Usuario getCriadoPor() {
        return criadoPor;
    }
    public boolean isTrocaSenhaObrigatoria() {
        return trocaSenhaObrigatoria;
    }
    public boolean isAtivo() {
        return status == Status.ATIVO;
    }
    public boolean isAdministrador() {
        return funcao == Funcao.ADMINISTRADOR;
    }
    public boolean isOrganizador() {
        return funcao == Funcao.ORGANIZADOR;
    }

    public boolean isArbitro() {
        return funcao == Funcao.ARBITRO;
    }

    public boolean temFuncao(Funcao funcaoEsperada) {
        return this.funcao == funcaoEsperada;
    }

    public static List<Usuario> getUsuarios() {
        return usuarios;
    }

    public static void setUsuarios(List<Usuario> listaUsuarios) {
        if (listaUsuarios == null) {
            usuarios = new ArrayList<>();
        } else {
            usuarios = listaUsuarios;
        }
    }

    // métodos listagem e busca
    public List<Usuario> carregarUsuariosNaTabela() {
        return listarUsuarios();
    }

    public List<Usuario> listarUsuarios() {
        return new ArrayList<>(usuarios);
    }

    public Usuario buscarUsuarioPorNome(String nome) {
        for (Usuario usuario : usuarios) {
            if (usuario.getNomeUsuario() != null && usuario.getNomeUsuario().equals(nome)) {
                return usuario;
            }
        }
        return null;
    }

    public Usuario buscarUsuarioPorEmail(String email) {
        for (Usuario usuario : usuarios) {
            if (usuario.getEmailUsuario() != null && usuario.getEmailUsuario().equalsIgnoreCase(email)) {
                return usuario;
            }
        }
        return null;
    }

    public Usuario buscarUsuarioPorIdentificacao(String cpf) {
        cpf = limparCpf(cpf);

        for (Usuario usuario : usuarios) {
            if (limparCpf(usuario.getCpf()).equals(cpf)) {
                return usuario;
            }
        }
        return null;
    }

    public List<Usuario> filtrarUsuariosPorPerfil() {
        return filtrarUsuariosPorPerfil(this.funcao);
    }

    public List<Usuario> filtrarUsuariosPorPerfil(Funcao funcao) {
        List<Usuario> usuariosFiltrados = new ArrayList<>();

        for (Usuario usuario : usuarios) {
            if (usuario.getFuncao() == funcao || funcao == Funcao.TODOS) {
                usuariosFiltrados.add(usuario);
            }
        }

        return usuariosFiltrados;
    }

    public List<Usuario> filtrarUsuariosPorStatus() {
        return filtrarUsuariosPorStatus(this.status);
    }

    public List<Usuario> filtrarUsuariosPorStatus(Status status) {
        List<Usuario> usuariosFiltrados = new ArrayList<>();

        for (Usuario usuario : usuarios) {
            if (usuario.getStatus() == status  || status == Status.TODOS) {
                usuariosFiltrados.add(usuario);
            }
        }

        return usuariosFiltrados;
    }

    public List<Usuario> ordenarUsuariosPorNome() {
        List<Usuario> usuariosOrdenados = new ArrayList<>(usuarios);

        usuariosOrdenados.sort(Comparator.comparing(
                Usuario::getNomeUsuario,
                Comparator.nullsLast(String::compareToIgnoreCase)
        ));

        return usuariosOrdenados;
    }

    public List<Usuario> limparFiltrosUsuarios() {
        return listarUsuarios();
    }

    // métodos cadastro
    public void abrirFormularioCadastroUsuario() {}

    public void cadastrarUsuario(String nomeUsuario, String emailUsuario,
                                 String senhaUsuario, Status status, Pais pais, Funcao funcao, String Cpf,
                                 Date dataCadastro, Usuario criadoPor) {
        this.nomeUsuario = nomeUsuario;
        this.emailUsuario = emailUsuario;
        this.senhaUsuario = senhaUsuario;
        this.status = status;
        this.funcao = funcao;
        this.Cpf = Cpf;
        this.dataCadastro = dataCadastro;
        this.criadoPor = criadoPor;

        salvarUsuario();
    }

    public boolean validarDadosUsuario() {
        if (!validarNomeUsuario()) {
            return false;
        }

        if (!validarEmailUsuario(emailUsuario)) {
            return false;
        }

        if (!validarEmailUnico(emailUsuario)) {
            return false;
        }

        if (!validarIdentificacaoUsuario(Cpf)) {
            return false;
        }

        if (!validarIdentificacaoUnica(Cpf)) {
            return false;
        }

        if (!validarPerfilSelecionado()) {
            return false;
        }

        if (!validarSenhaUsuario()) {
            return false;
        }

        if (status == null) {
            return false;
        }
        return true;
    }

    public boolean validarNomeUsuario() {
        if (nomeUsuario == null) {
            return false;
        }

        nomeUsuario = nomeUsuario.trim();

        if (nomeUsuario.length() < 3 || nomeUsuario.length() > 80) {
            return false;
        }

        return true;
    }

    public boolean validarEmailUsuario(String email) {
        if (email == null) {
            return false;
        }

        email = email.trim().toLowerCase();

        if (email.length() > 64 || email.length() < 4) {
            return false;
        }

        if (email.contains(" ")) {
            return false;
        }

        if (email.endsWith("@gmail.com") || email.endsWith("@outlook.com") ||
                email.endsWith("@hotmail.com") || email.endsWith("@yahoo.com.br") ||
                email.endsWith("@icloud.com")) {
            return true;
        }

        return true;
    }

    public boolean validarEmailUnico(String email) {
        if (email == null) {
            return false;
        }

        for (Usuario usuario : usuarios) {
            if (usuario != this && usuario.getEmailUsuario() != null &&
                    usuario.getEmailUsuario().equalsIgnoreCase(email)) {
                return false;
            }
        }

        return true;
    }

    public boolean validarIdentificacaoUsuario(String Cpf) {
        if (Cpf == null) {
            return false;
        }

        Cpf = limparCpf(Cpf);

        if (Cpf.length() != 11) {
            return false;
        }

        for (int i = 0; i < Cpf.length(); i++) {
            if (!Character.isDigit(Cpf.charAt(i))) {
                return false;
            }
        }

        boolean todosIguais = true;
        for (int i = 1; i < Cpf.length(); i++) {
            if (Cpf.charAt(i) != Cpf.charAt(0)) {
                todosIguais = false;
                break;
            }
        }

        if (todosIguais) {
            return false;
        }

        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += Character.getNumericValue(Cpf.charAt(i)) * (10 - i);
        }

        int primeiroDigito = 11 - (soma % 11);
        if (primeiroDigito >= 10) {
            primeiroDigito = 0;
        }

        if (primeiroDigito != Character.getNumericValue(Cpf.charAt(9))) {
            return false;
        }

        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += Character.getNumericValue(Cpf.charAt(i)) * (11 - i);
        }

        int segundoDigito = 11 - (soma % 11);
        if (segundoDigito >= 10) {
            segundoDigito = 0;
        }

        if (segundoDigito != Character.getNumericValue(Cpf.charAt(10))) {
            return false;
        }

        return true;
    }

    public boolean validarIdentificacaoUnica(String Cpf) {
        Cpf = limparCpf(Cpf);

        for (Usuario usuario : usuarios) {
            if (usuario != this && limparCpf(usuario.getCpf()).equals(Cpf)) {
                return false;
            }
        }

        return true;
    }

    public boolean validarPerfilSelecionado() {
        return funcao != null;
    }

    public boolean validarSenhaUsuario() {
        return validarSenhaTexto(senhaUsuario);
    }

    public String gerarSenhaProvisoria() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String senhaProvisoria = "";
        Random random = new Random();

        for (int i = 0; i < 8; i++) {
            int posicao = random.nextInt(caracteres.length());
            senhaProvisoria += caracteres.charAt(posicao);
        }

        this.senhaUsuario = senhaProvisoria;
        return senhaProvisoria;
    }

    public void marcarTrocaSenhaObrigatoria() {
        this.trocaSenhaObrigatoria = true;
    }

    public void salvarUsuario() {
        if (dataCadastro == null) {
            dataCadastro = new Date();
        }

        if (validarDadosUsuario() && !usuarios.contains(this)) {
            usuarios.add(this);
        }

        try {
            PersistenciaUsuarios persistencia = new PersistenciaUsuarios();
            persistencia.salvarUsuario(this);
        } catch (PersistenciaException e) {
            System.out.println("erro ao salvar usuário: " + e.getMessage());
        }
    }

    public static boolean salvarUsuarioNoBanco(Usuario novoUsuario) {
        if (novoUsuario.validarDadosUsuario()) {
            try {
                copamundo.usuarios.persistencia.PersistenciaUsuarios persistencia = new copamundo.usuarios.persistencia.PersistenciaUsuarios();

                persistencia.salvarUsuario(novoUsuario);

                usuarios = persistencia.carregarUsuarios();

                return true;
            } catch (copamundo.usuarios.excecoes.PersistenciaException e) {
                System.out.println("Erro ao gravar dados no arquivo: " + e.getMessage());
                return false;
            }
        }
        return false;
    }

    public List<Usuario> atualizarTabelaUsuarios() {
        return listarUsuarios();
    }

    // métodos edição
    public boolean validarSenha(String senha) {
        if (senhaUsuario != null && senhaUsuario.equals(senha)) {
            return true;
        }

        return false;
    }

    public void abrirFormularioEdicaoUsuario() {}

    public void editarUsuario() {}

    public boolean validarEdicaoUsuario() {
        return validarDadosUsuario();
    }

    public boolean alterarNomeUsuario(String nome, String senha, Usuario responsavel) {
        if (nome == null || nome.trim().length() < 3) {
            return false;
        }

        if (validarPermissaoAlteracao(senha, responsavel)) {
            this.nomeUsuario = nome.trim();
            registrarAlteracaoUsuario();
            return true;
        }

        return false;
    }

    public boolean alterarEmailUsuario(String email, String senha, Usuario responsavel) {
        if (!validarEmailUsuario(email) || !validarEmailUnico(email)) {
            return false;
        }

        if (validarPermissaoAlteracao(senha, responsavel)) {
            this.emailUsuario = email.trim().toLowerCase();
            registrarAlteracaoUsuario();
            return true;
        }

        return false;
    }

    public boolean alterarFuncaoUsuario(Funcao funcao, String senha, Usuario responsavel) {
        if (funcao == null) {
            return false;
        }

        if (validarPermissaoAlteracao(senha, responsavel)) {
            this.funcao = funcao;
            registrarAlteracaoUsuario();
            return true;
        }

        return false;
    }

    public boolean alterarStatusUsuario(Status status, String senha, Usuario responsavel) {
        if (status == null) {
            return false;
        }

        if (this.isAdministrador() && status == Status.INATIVO && verificarSeEhUltimoAdministrador()) {
            return false;
        }

        if (validarPermissaoAlteracao(senha, responsavel)) {
            this.status = status;
            registrarAlteracaoUsuario();
            return true;
        }

        return false;
    }

    public boolean validacaoSenhaAnterior(String senhaUsuario) {
        return this.senhaUsuario.equals(senhaUsuario);
    }

    public boolean redefinirSenhaUsuario(String senhaUsuario, String senha, Usuario responsavel) {
        if (!validarSenhaTexto(senhaUsuario)) {
            return false;
        }

        if (validarPermissaoAlteracao(senha, responsavel)) {
            this.senhaUsuario = senhaUsuario;
            this.trocaSenhaObrigatoria = true;
            registrarAlteracaoUsuario();

            try {
                PersistenciaUsuarios persistencia = new PersistenciaUsuarios();
                persistencia.salvarUsuario(this);
            } catch (PersistenciaException e) {
                System.out.println("erro ao salvar nova senha: " + e.getMessage());
            }
            return true;
        }

        return false;
    }

    public boolean validarPermissaoAlteracao(String senha, Usuario responsavel) {
        if (responsavel == null) {
            return false;
        }

        for (Usuario usuario : usuarios) {
            if (limparCpf(usuario.getCpf()).equals(limparCpf(responsavel.getCpf()))) {
                if (usuario.validarSenha(senha)) {
                    if (usuario.isAtivo()) {
                        if (usuario.isAdministrador()) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public void registrarAlteracaoUsuario() {}

    // métodos exclusão/desativação
    public boolean confirmarExclusaoUsuario() {
        return verificarSePodeExcluirUsuario();
    }

    public boolean verificarSePodeExcluirUsuario() {
        if (verificarSeEhUltimoAdministrador()) {
            return false;
        }

        if (verificarDependenciasUsuario()) {
            return false;
        }

        return true;
    }

    public boolean verificarSeEhUltimoAdministrador() {
        if (!this.isAdministrador() || !this.isAtivo()) {
            return false;
        }

        int quantidadeAdministradoresAtivos = 0;

        for (Usuario usuario : usuarios) {
            if (usuario.isAdministrador() && usuario.isAtivo()) {
                quantidadeAdministradoresAtivos++;
            }
        }

        return quantidadeAdministradoresAtivos <= 1;
    }

    public boolean verificarDependenciasUsuario() {
        for (Usuario usuario : usuarios) {
            if (usuario.getCriadoPor() == this) {
                return true;
            }
        }

        return false;
    }

    public boolean desativarUsuario() {
        if (verificarSeEhUltimoAdministrador()) {
            return false;
        }

        this.status = Status.INATIVO;
        return true;
    }

    public boolean excluirUsuario() {
        if (verificarSePodeExcluirUsuario()) {
            usuarios.remove(this);
            return true;
        }

        return false;
    }

    public boolean excluirLogicamenteUsuario() {
        return desativarUsuario();
    }

    public List<Usuario> atualizarTabelaAposExclusao() {
        return listarUsuarios();
    }

    private String limparCpf(String Cpf) {
        if (Cpf == null) {
            return "";
        }
        return Cpf.replace(".", "").replace("-", "").trim();
    }

    private boolean validarSenhaTexto(String senha) {
        if (senha == null) {
            System.out.println("Digite uma senha!");
            return false;
        }

        if (senha.length() < 8 || senha.length() > 20) {
            System.out.println("Tamanho inválido de senha!");
            return false;
        }

        boolean temNumero = false;
        boolean temLetras = false;

        for (int i = 0; i < senha.length(); i++) {
            if (Character.isDigit(senha.charAt(i))) {
                temNumero = true;
            } else if (Character.isLetter(senha.charAt(i))) {
                temLetras = true;
            }
        }

        if (!temNumero) {
            System.out.println("A senha deve ter no mínimo um número!");
            return false;
        } else if (!temLetras) {
            System.out.println("A senha deve ter no mínimo uma letra!");
            return false;
        }
        return true;
    }
}