package copamundo.partidas.visao;
import copamundo.comum.Fase;
import copamundo.comum.Partida;
import copamundo.comum.Selecao;
import copamundo.comum.StatusPartida;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

import static copamundo.partidas.repositorio.PartidaRepositorio.listaPartidas;

public class TelaConsultaPartidasController {
    // AJUSTAR METODOS

    @FXML
    private ComboBox<?> SeletorFase;

    @FXML
    private Button btnFiltrarConsulta;

    @FXML
    private Button btnLimparFiltros;

    @FXML
    private Button btnTelaCadastroPartidas;

    @FXML
    private Button btnTelaEditarPartida;

    @FXML
    private Button btnTelaRegistroResultados;

    @FXML
    private TableColumn<?, ?> colunaData;

    @FXML
    private TableColumn<?, ?> colunaEstadio;

    @FXML
    private TableColumn<?, ?> colunaFase;

    @FXML
    private TableColumn<?, ?> colunaHorario;

    @FXML
    private TableColumn<?, ?> colunaPlacar;

    @FXML
    private TableColumn<?, ?> colunaSelecoes;

    @FXML
    private TableColumn<?, ?> colunaStatus;

    @FXML
    private ComboBox<?> seletorSelecao;

    @FXML
    private ComboBox<?> seletorStatus;

    @FXML
    private TableView<?> tabelaInteira;

    @FXML
    private TextField textoData;

    @FXML
    void filtrarListaPartidas(javafx.event.ActionEvent event) {

    }

    @FXML
    void irTelaCadastroPartidas(javafx.event.ActionEvent event) throws IOException {

    }

    @FXML
    void irTelaEditarPartida(javafx.event.ActionEvent event) throws IOException {

    }

    @FXML
    void irTelaRegistroResultados(javafx.event.ActionEvent event) throws IOException {

    }

    @FXML
    void limparFiltros(javafx.event.ActionEvent event) {

    }

    public ArrayList<Partida>  listarPartidas() {
        return listaPartidas;
    }

    public ArrayList<Partida>  listaPorSelecao (Selecao selecao){
        ArrayList<Partida> listaFiltradaPartidas = new ArrayList<Partida>();
        for (int i = 0; i < listaPartidas.size(); i++) {
            if (listaPartidas.get(i).getSelecao1().equals(selecao) || listaPartidas.get(i).getSelecao2().equals(selecao)) {
                listaFiltradaPartidas.add(listaPartidas.get(i));
            }
        }
        return listaFiltradaPartidas;
    }

    public ArrayList<Partida> listaPorData (String data){
        ArrayList<Partida> listaFiltradaPartidas = new ArrayList<Partida>();
        for (int i = 0; i < listaPartidas.size(); i++) {
            if (listaPartidas.get(i).getDataPartida().equals(data)) {
                listaFiltradaPartidas.add(listaPartidas.get(i));
            }
        }
        return listaFiltradaPartidas;
    }

    public ArrayList<Partida>  listaPorFase (Fase fase){
        ArrayList<Partida> listaFiltradaPartidas = new ArrayList<Partida>();
        for (int i = 0; i < listaPartidas.size(); i++) {
            if (listaPartidas.get(i).getFase().equals(fase)) {
                listaFiltradaPartidas.add(listaPartidas.get(i));
            }

        }
        return listaFiltradaPartidas;
    }

    public ArrayList<Partida> listaPorStatusPartida (StatusPartida status){
        ArrayList<Partida> listaFiltradaPartidas = new ArrayList<Partida>();
        for (int i = 0; i < listaPartidas.size(); i++) {
            if (listaPartidas.get(i).getStatusPartida().equals(status)) {
                listaFiltradaPartidas.add(listaPartidas.get(i));
            }

        }
        return listaFiltradaPartidas;
    }
}
