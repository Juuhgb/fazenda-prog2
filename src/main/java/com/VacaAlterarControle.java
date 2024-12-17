package com;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import modelo.Vaca;
import util.Dao;

public class VacaAlterarControle {

    @FXML
    private TextField campoBrincoAtual;

    @FXML
    private TextField campoNovoBrinco;

    @FXML
    private TextField campoNovoNome;

    @FXML
    private TextField campoNovaRaca;

    private Dao<Vaca> dao;
    private Vaca vaca;

    @FXML
    private void initialize() {
        dao = new Dao<>(Vaca.class);
    }

    @FXML
    private void buscarVaca() {
        String brincoAtual = campoBrincoAtual.getText().toUpperCase();
        if (brincoAtual.isBlank()) {
            mostrarErro("Digite o número do brinco atual para buscar.");
            return;
        }

        vaca = dao.buscarPorChave("brinco", brincoAtual);
        if (vaca == null) {
            mostrarErro("Vaca não encontrada.");
            return;
        }

        // Preenche os campos com os dados da vaca
        campoNovoBrinco.setText(vaca.getBrinco());
        campoNovoNome.setText(vaca.getNome());
        campoNovaRaca.setText(vaca.getRaca());
    }

    @FXML
    private void salvarAlteracoes() {
        if (vaca == null) {
            mostrarErro("Nenhuma vaca foi selecionada para alteração.");
            return;
        }

        String novoBrinco = campoNovoBrinco.getText().toUpperCase();
        String novoNome = campoNovoNome.getText();
        String novaRaca = campoNovaRaca.getText();

        if (novoBrinco.isBlank() || novoNome.isBlank() || novaRaca.isBlank()) {
            mostrarErro("Todos os campos devem ser preenchidos.");
            return;
        }

        // Verificar se o novo brinco já está em uso e não pertence à vaca atual
        Vaca vacaExistente = dao.buscarPorChave("brinco", novoBrinco);
        if (vacaExistente != null && !vacaExistente.getBrinco().equals(vaca.getBrinco())) {
            mostrarErro("O novo brinco já está em uso.");
            return;
        }

        // Atualizar a vaca
        vaca.setBrinco(novoBrinco);
        vaca.setNome(novoNome);
        vaca.setRaca(novaRaca);

        dao.alterar("brinco", campoBrincoAtual.getText().toUpperCase(), vaca);
        mostrarSucesso("Vaca alterada com sucesso.");
        limparCampos();
    }

    @FXML
    private void voltarMenu() throws IOException {
        App.setRoot("menu");
    }

    private void limparCampos() {
        campoBrincoAtual.setText("");
        campoNovoBrinco.setText("");
        campoNovoNome.setText("");
        campoNovaRaca.setText("");
        vaca = null;
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.show();
    }

    private void mostrarSucesso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.show();
    }
}
