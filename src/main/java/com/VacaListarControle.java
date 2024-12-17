package com;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import modelo.Vaca;
import util.Dao;

public class VacaListarControle {

    @FXML
    private TableView<Vaca> tabelaVacas;

    @FXML
    private TableColumn<Vaca, String> colunaBrinco;

    @FXML
    private TableColumn<Vaca, String> colunaNome;

    @FXML
    private TableColumn<Vaca, String> colunaRaca;

    private Dao<Vaca> dao;

    @FXML
    private void initialize() {
        dao = new Dao<>(Vaca.class);
        configurarTabela();
        carregarDados();
    }

    private void configurarTabela() {
        colunaBrinco.setCellValueFactory(new PropertyValueFactory<>("brinco"));
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaRaca.setCellValueFactory(new PropertyValueFactory<>("raca"));
    }

    private void carregarDados() {
        try {
            ObservableList<Vaca> vacas = FXCollections.observableArrayList(dao.listarTodos());
            tabelaVacas.setItems(vacas);
        } catch (Exception e) {
            mostrarErro("Erro ao carregar os dados: " + e.getMessage());
        }
    }

    @FXML
    public void voltarMenu() throws IOException {
        App.setRoot("menu");
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.show();
    }
}
