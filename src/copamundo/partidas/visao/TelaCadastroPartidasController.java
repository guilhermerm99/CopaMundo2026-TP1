package copamundo.partidas.visao;

import copamundo.comum.Fase;
import copamundo.comum.Partida;
import copamundo.comum.Selecao;
import copamundo.comum.StatusPartida;
import copamundo.comum.Estadio;
import copamundo.estadios.controle.ArbitroController;
import copamundo.estadios.controle.EstadioController;
import copamundo.estadios.excecoes.PersistenciaException;
import copamundo.estadios.excecoes.RegraNegocioException;
import copamundo.estadios.modelo.Arbitro;
import copamundo.partidas.excecoes.PartidaMesmaDataException;
import copamundo.partidas.repositorio.PartidaRepositorio;
import copamundo.partidas.excecoes.PartidaNaoEncontradaException;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import java.util.List;
import java.util.ArrayList;
import java.awt.*;
import java.io.IOException;

//import static copamundo.partidas.repositorio.PartidaRepositorio.listaPartidas; RETIRAR DEPOIS !!!!!!!!!

public class TelaCadastroPartidasController {
    // AJUSTAR MÉTODOS - GARANTIR QUE NÃO ESTÃO REPETIDOS

    @FXML
    private Button btnCancelarCadastro;

    @FXML
    private Button btnSalvarCadastroPartida;

    @FXML
    private Button btnTelaConsultaPartidas;

    @FXML
    private Button btnTelaRegistroResultado;

    @FXML
    private ComboBox<?> seletorEstadio;

    @FXML
    private ComboBox<?> seletorSelecao1;

    @FXML
    private ComboBox<?> seletorSelecao2;

    @FXML
    private TextField textoData;

    @FXML
    private TextField textoHorario;

    @FXML
    private ComboBox<?> seletorArbitro;

    @FXML
    void irTelaConsultaPartidas(javafx.event.ActionEvent event) throws IOException {

    }

    @FXML
    void irTelaRegistroResultado(javafx.event.ActionEvent event) throws IOException {

    }

    @FXML
    void salvarPartida(javafx.event.ActionEvent event) {

    }

    @FXML
    void voltarMenuInicial(javafx.event.ActionEvent event) throws IOException {

    }


    private final EstadioController estadioController = new EstadioController();
    private final ArbitroController arbitroController = new ArbitroController();

    public void designarArbitroPrincipal(Partida partida, Arbitro arbitro)
            throws PersistenciaException, RegraNegocioException {
        if (partida == null) {
            throw new RegraNegocioException("Selecione uma partida.");
        }

        arbitroController.validarNacionalidadeParaPartida(arbitro, partida);
        partida.setArbitroPrincipal(arbitro);
        //repositorioPartidas.salvar(partida);
    }

    public void CadastrarPartida(String dataPartida, String horarioPartida, Estadio estadioPartida, Selecao selecao1, Selecao selecao2, Fase fase,
                                 StatusPartida status, Arbitro arbitro) throws IOException, ClassNotFoundException, PersistenciaException, RegraNegocioException {
        List<Partida> listaPartidas = PartidaRepositorio.carregarListaPartidas();

        if (selecao1 != selecao2) {
            for (Partida p : listaPartidas) {
                if ((p.getSelecao1() == selecao1) || (p.getSelecao1() == selecao2) || (p.getSelecao2() == selecao1) || (p.getSelecao2() == selecao2)) {
                    if (p.getDataPartida().equals(dataPartida)) {
                        throw new PartidaMesmaDataException("Uma seleção já possui partida nesta data.");
                    }
                }
            }
            Partida partida = new Partida(dataPartida, horarioPartida, estadioPartida, selecao1, selecao2, fase, status, arbitro);

            estadioController.validarEstadioDisponivel(partida);
            designarArbitroPrincipal(partida, arbitro);

            listaPartidas.add(partida);
        }

        PartidaRepositorio.salvarListaPartidas(listaPartidas);
    }


    public String ExcluirPartida(String id) throws IOException {
        try {
            List<Partida> listaPartidas = PartidaRepositorio.carregarListaPartidas();

            for (int i = 0; i < listaPartidas.size(); i++) {
                if (listaPartidas.get(i).getId().equals(id)) {
                    listaPartidas.remove(i);
                    PartidaRepositorio.salvarListaPartidas(listaPartidas);
                    return "Partida removida com sucesso!";
                }
            }
            throw new PartidaNaoEncontradaException("Partida não encontrada");

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return "Erro ao acessar o arquivo";
        }

    }


}
