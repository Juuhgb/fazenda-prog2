package com; 

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import modelo.Producao;
import modelo.Vaca;
import util.Dao;

import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;

public class ListarProducaoVacaControle {

    @FXML
    private TextField campoBrinco;

    @FXML
    private ComboBox<String> comboBrincos;

    @FXML
    private ComboBox<String> comboMeses;

    @FXML
    private DatePicker datePicker;

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

        // Configurar tabela
        colunaBrinco.setCellValueFactory(new PropertyValueFactory<>("brinco"));
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome")); // Relacionado à Vaca
        colunaData.setCellValueFactory(new PropertyValueFactory<>("data"));
        colunaQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

        tabelaProducao.setItems(listaProducao);

        // Preencher comboBrincos com todos os brincos disponíveis
        carregarBrincos();

        // Preencher comboMeses com os meses
        carregarMeses();
    }

    private void carregarBrincos() {
        List<Vaca> vacas = daoVaca.listarTodos();
        ObservableList<String> brincos = FXCollections.observableArrayList();
        for (Vaca vaca : vacas) {
            brincos.add(vaca.getBrinco());
        }
        comboBrincos.setItems(brincos);
    }

    private void carregarMeses() {
        ObservableList<String> meses = FXCollections.observableArrayList();
        for (Month mes : Month.values()) {
            meses.add(mes.name());
        }
        comboMeses.setItems(meses);
    }

    private boolean novaBuscaAtiva = false;

    @FXML
    private void selecionarBrinco() {
        if (novaBuscaAtiva) return; // Se a nova busca está ativa, não realizar a validação

        String brinco = comboBrincos.getValue();
        if (brinco == null) {
            mostrarErro("Selecione um brinco válido.");
            return;
        }
    }
    
    @FXML
    private void buscarPorBrinco() {
        String brinco = campoBrinco.getText().toUpperCase();

        // Verifica se o brinco está vazio
        if (brinco.isBlank()) {
            mostrarErro("Digite um brinco válido para buscar.");
            return;
        }

        // Verifica se a vaca com o brinco existe
        Vaca vacaEncontrada = daoVaca.buscarPorChave("brinco", brinco);
        if (vacaEncontrada == null) {
            mostrarErro("Nenhuma vaca encontrada com o brinco: " + brinco);
            return;
        }

        // Exibe mensagem de sucesso
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Vaca Encontrada");
        alert.setHeaderText(null);
        alert.setContentText("A vaca com brinco " + brinco + " foi encontrada. Selecione um mês ou data para filtrar a produção.");
        alert.show();

        // Salva o brinco encontrado para uso nos filtros subsequentes
        comboBrincos.setValue(brinco); // Opcional, para sincronizar o ComboBox com o TextField
    }

    @FXML
    private void filtrarPorMes() {
        String brincoSelecionado = campoBrinco.getText().isBlank() ? comboBrincos.getValue() : campoBrinco.getText().toUpperCase();
        String mesSelecionado = comboMeses.getValue();

        if (brincoSelecionado == null || brincoSelecionado.isBlank()) {
            mostrarErro("Selecione ou insira um brinco válido.");
            return;
        }

        if (mesSelecionado == null) {
            mostrarErro("Selecione um mês válido.");
            return;
        }

        Month mes = Month.valueOf(mesSelecionado.toUpperCase());

        List<Producao> producoes = daoProducao.listarTodos();
        List<Producao> producoesFiltradas = producoes.stream()
                .filter(p -> p.getBrinco() != null && p.getBrinco().equals(brincoSelecionado) && p.getData().getMonth() == mes)
                .collect(Collectors.toList());

        atualizarTabela(producoesFiltradas);
    }

    @FXML
    private void filtrarPorDia() {
        LocalDate data = datePicker.getValue();
        String brincoSelecionado = campoBrinco.getText().isBlank() ? comboBrincos.getValue() : campoBrinco.getText().toUpperCase();

        if (brincoSelecionado == null || brincoSelecionado.isBlank()) {
            mostrarErro("Selecione ou insira um brinco válido.");
            return;
        }

        if (data == null) {
            mostrarErro("Selecione uma data válida.");
            return;
        }

        List<Producao> producoes = daoProducao.listarTodos();
        List<Producao> producoesFiltradas = producoes.stream()
                .filter(p -> p.getBrinco() != null && p.getBrinco().equals(brincoSelecionado) && p.getData().isEqual(data))
                .collect(Collectors.toList());

        atualizarTabela(producoesFiltradas);
    }


    @FXML
   private void novaBusca() {
       novaBuscaAtiva = true; // Marcar início da nova busca

       // Limpar a tabela
       listaProducao.clear();

       // Limpar os campos de entrada
       campoBrinco.clear(); // Limpar o campo de texto do brinco
       comboBrincos.setValue(null);  // Desmarcar a seleção do combo de brincos
       comboMeses.setValue(null);    // Desmarcar a seleção do combo de meses
       datePicker.setValue(null);    // Limpar a data selecionada

       // Recarregar os itens do combo de brincos e meses
       carregarBrincos();
       carregarMeses();

       // Marcar que a nova busca terminou somente aqui, após as operações
       novaBuscaAtiva = false;
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
                producao.setNome(vaca.getNome()); // Definir nome para exibição
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