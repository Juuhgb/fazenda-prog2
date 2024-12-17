package com;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import modelo.Producao;
import modelo.Vaca;
import util.Dao;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;

public class ProducaoFazendaTotalControle {

    @FXML
    private ComboBox<String> comboMeses;

    @FXML
    private DatePicker datePickerDia;

    @FXML
    private TableView<Producao> tabelaProducao;

    @FXML
    private TableColumn<Producao, String> colunaBrinco;

    @FXML
    private TableColumn<Producao, String> colunaNome;

    @FXML
    private TableColumn<Producao, LocalDate> colunaData;

    @FXML
    private TableColumn<Producao, Double> colunaQuantidade;

    private Dao<Vaca> daoVaca;
    private Dao<Producao> daoProducao;
    private ObservableList<Producao> listaProducao;

    @FXML
    private void initialize() {
        daoVaca = new Dao<>(Vaca.class);
        daoProducao = new Dao<>(Producao.class);
        listaProducao = FXCollections.observableArrayList();

        // Configurar a tabela
        colunaBrinco.setCellValueFactory(new PropertyValueFactory<>("brinco"));
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaData.setCellValueFactory(new PropertyValueFactory<>("data"));
        colunaQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

        tabelaProducao.setItems(listaProducao);

        // Preencher comboMeses com os meses
        carregarMeses();
    }

    private void carregarMeses() {
        ObservableList<String> meses = FXCollections.observableArrayList();
        for (Month mes : Month.values()) {
            meses.add(mes.name());
        }
        comboMeses.setItems(meses);
    }

    @FXML
    private void filtrarPorMes() {
        String mesSelecionado = comboMeses.getValue();

        if (mesSelecionado == null) {
            mostrarErro("Selecione uma data válida.");
            return;
        }

        Month mes = Month.valueOf(mesSelecionado.toUpperCase());

        List<Producao> producoes = daoProducao.listarTodos();
        List<Producao> producoesFiltradas = producoes.stream()
                .filter(p -> p.getData().getMonth() == mes)
                .collect(Collectors.toList());

        atualizarTabela(producoesFiltradas);
    }

    @FXML
    private void filtrarPorDia() {
        LocalDate data = datePickerDia.getValue();

        if (data == null) {
            mostrarErro("Selecione uma data válida.");
            return;
        }

        List<Producao> producoes = daoProducao.listarTodos();
        List<Producao> producoesFiltradas = producoes.stream()
                .filter(p -> p.getData().isEqual(data))
                .collect(Collectors.toList());

        atualizarTabela(producoesFiltradas);
    }

    @FXML
    private void novaBusca() {
        // Limpar a tabela
        listaProducao.clear();

        // Limpar os campos de entrada
        comboMeses.setValue(null);
        datePickerDia.setValue(null);

        // Recarregar os meses
        carregarMeses();
    }

    @FXML
    private void voltar() {
        try {
            App.setRoot("menu");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void atualizarTabela(List<Producao> producoes) {
        listaProducao.clear();
        for (Producao producao : producoes) {
            Vaca vaca = daoVaca.buscarPorChave("brinco", producao.getBrinco());
            if (vaca != null) {
                producao.setNome(vaca.getNome()); // Definir o nome para exibição
            }
            listaProducao.add(producao);
        }
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.show();
    }
}
