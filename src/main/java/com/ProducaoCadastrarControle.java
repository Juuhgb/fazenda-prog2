package com;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import modelo.Producao;
import modelo.Vaca;
import util.Dao;

import java.time.LocalDate;

public class ProducaoCadastrarControle {

    @FXML
    private TextField campoBrinco;

    @FXML
    private DatePicker campoData;

    @FXML
    private TextField campoProducao;

    private Dao<Producao> daoProducao;
    private Dao<Vaca> daoVaca;

    @FXML
    private void initialize() {
        daoProducao = new Dao<>(Producao.class);
        daoVaca = new Dao<>(Vaca.class);
    }

    @FXML
    private void cadastrar() {
        try {
            String brinco = campoBrinco.getText().toUpperCase();
            LocalDate data = campoData.getValue();
            double quantidade;

            // Validação dos campos
            if (brinco.isBlank() || data == null || campoProducao.getText().isBlank()) {
                mostrarErro("Preencha todos os campos!");
                return;
            }

            try {
                quantidade = Double.parseDouble(campoProducao.getText());
                if (quantidade <= 0) {
                    mostrarErro("A quantidade deve ser maior que zero.");
                    return;
                }
            } catch (NumberFormatException e) {
                mostrarErro("Digite um valor válido para a quantidade.");
                return;
            }

            // Verificar se a vaca existe
            Vaca vaca = daoVaca.buscarPorChave("brinco", brinco);
            if (vaca == null) {
                mostrarErro("Não foi encontrada uma vaca com o brinco informado.");
                return;
            }

            // Cadastrar a produção
            Producao producao = new Producao(brinco, data, quantidade);
            daoProducao.inserir(producao);

            mostrarSucesso("Produção cadastrada com sucesso!");
            limparCampos();

        } catch (Exception e) {
            mostrarErro("Ocorreu um erro ao cadastrar a produção: " + e.getMessage());
        }
    }

    @FXML
    private void cancelar() {
        try {
            App.setRoot("menu");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void limparCampos() {
        campoBrinco.clear();
        campoData.setValue(null);
        campoProducao.clear();
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.show();
    }

    private void mostrarSucesso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.show();
    }
}
