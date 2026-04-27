# Sistema de Gestão da Copa do Mundo 2026

Projeto da disciplina **Técnicas de Programação 1 (CIC0197)** – UnB, 2026-1.

## 👥 Equipe
| Nome | Módulo | GitHub |
|------|--------|--------|
| Aluno 1 | Administração e Gestão | @user1 |
| Aluno 2 | Seleções e Jogadores | @guilhermerm99 |
| Aluno 3 | Estádios e Arbitragem | @user3 |
| Aluno 4 | Partidas | @user4 |

**Profª:** Roberta Barbosa Oliveira

## 📦 Descrição
Sistema desktop para automatizar os principais processos de organização da Copa do Mundo de 2026: cadastro de seleções e jogadores, estádios, arbitragem, partidas, além de controle de acesso e relatórios.

O sistema é desenvolvido em **Java** com interface gráfica em **JavaFX**, utilizando arquivos CSV para persistência. A arquitetura segue o modelo de camadas (visão, controle, persistência) e aplica princípios de orientação a objetos, como encapsulamento, herança e polimorfismo.

## 🛠️ Pré‑requisitos
- **JDK 21** ou superior ([OpenJDK](https://jdk.java.net/21/))
- **JavaFX SDK 21** ([Gluon](https://gluonhq.com/products/javafx/))
- **IntelliJ IDEA** (recomendado) ou qualquer IDE com suporte a JavaFX
- (Opcional) **Scene Builder** para editar arquivos `.fxml` ([Download](https://gluonhq.com/products/scene-builder/))

## ⚙️ Configuração do ambiente

1. **Clone este repositório**
   ```bash
   git clone [https://github.com/guilhermerm99/CopaMundo2026-TP1.git](https://github.com/guilhermerm99/CopaMundo2026-TP1.git)
   ```

2. **Configure o JavaFX no IntelliJ**
   - Vá em `File → Project Structure → Libraries`.
   - Adicione uma nova biblioteca do tipo Java e selecione todos os `.jar` da pasta `lib` do seu JavaFX SDK.
   - Em `Run → Edit Configurations`, adicione as `VM options` (ajuste o caminho conforme sua instalação):
     ```text
     --module-path "C:\Users\seu_usuario\Documents\javafx-sdk-21\lib" --add-modules javafx.controls,javafx.fxml --enable-native-access=javafx.graphics
     ```
   - Certifique-se de que o diretório `src` esteja marcado como **Sources Root** (azul).

3. **Compile e execute**
   - Execute a classe `copamundo.Main`. A janela do módulo de Seleções e Jogadores deve abrir.

## 📁 Estrutura do projeto

```text
src/
└── copamundo/
    ├── Main.java                      # Classe principal
    ├── comum/                         # Classes de domínio compartilhadas
    │   ├── Usuario.java
    │   ├── Selecao.java
    │   ├── Jogador.java
    │   ├── Estadio.java
    │   └── Partida.java
    ├── administracao/                 # Módulo de gestão de usuários (Aluno 1)
    │   ├── controle/
    │   ├── visao/
    │   └── persistencia/
    ├── selecoes/                      # Módulo de seleções e jogadores (Aluno 2)
    │   ├── controle/
    │   │   └── GestaoSelecoesJogadores.java
    │   ├── visao/
    │   │   ├── estilo.css
    │   │   ├── TelaMenuSelecoesJogadores.fxml
    │   │   ├── TelaMenuSelecoesJogadoresController.java
    │   │   ├── TelaCadastroSelecao.fxml
    │   │   ├── TelaCadastroSelecaoController.java
    │   │   ├── TelaCadastroJogador.fxml
    │   │   ├── TelaCadastroJogadorController.java
    │   │   ├── TelaConsultaSelecoesJogadores.fxml
    │   │   └── TelaConsultaSelecoesJogadoresController.java
    │   ├── persistencia/
    │   │   └── PersistenciaSelecoesJogadores.java
    │   └── excecoes/
    │       └── ElencoCheioException.java
    ├── partidas/                      # Módulo de partidas (Aluno 4)
    └── estadios/                      # Módulo de estádios e arbitragem (Aluno 3)
```

## 📝 Estrutura do relatório
O documento final será escrito em **LaTeX** (Overleaf). Etapas:
- Estrutura Inicial (protótipos)
- Design e Modelagem (diagramas)
- Implementação da Lógica
- Integração

## 🚀 Funcionalidades atuais (Etapa 1)
- Estrutura de pacotes definida.
- Telas prototipadas com JavaFX e Scene Builder:
  - Menu do módulo Seleções e Jogadores.
  - Cadastro de Seleções.
  - Cadastro de Jogadores.
  - Consultas (com filtros por grupo, posição e status).
- Navegação funcional entre as telas (sem lógica de negócio).

## 📌 Próximas etapas
- Implementar regras de negócio (validações, CRUD).
- Desenvolver a camada de persistência com arquivos CSV.
- Integrar todos os módulos da equipe.
- Gerar o relatório final.

## 📚 Referências
- DEITEL, P.; DEITEL, H. Java: Como Programar. Pearson.
- OpenJFX Documentation: https://openjfx.io/
- Scene Builder: https://gluonhq.com/products/scene-builder/

---
**Universidade de Brasília – Campus Darcy Ribeiro** Departamento de Ciência da Computação  
2025/2
