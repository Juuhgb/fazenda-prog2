package com;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import modelo.Vaca;
import util.Dao;

public class VacaExcluirControle {

    @FXML
    private TextField campoBrinco;

    private Dao<Vaca> dao;

    @FXML
    private void initialize() {
        dao = new Dao<>(Vaca.class);
    }

    @FXML
    private void excluirVaca() {
        String brinco = campoBrinco.getText().toUpperCase();
        Vaca vaca = dao.buscarPorChave("brinco", brinco);

        
        if (vaca != null) {
            // Exclui a vaca do banco de dados
            boolean sucesso = dao.excluir("brinco", brinco);

            if (sucesso) {
                mostrarSucesso("Vaca excluída com sucesso.");
            } else {
                mostrarErro("Erro ao excluir a vaca.");
            }
        } else {
            mostrarErro("Vaca não encontrada.");
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

    private void mostrarSucesso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.show();
    }
}
