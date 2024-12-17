package com;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import modelo.Usuario;
import util.Dao;

public class UsuarioIncluirControle {
    
    @FXML
    private TextField campoLogin;
    @FXML
    private TextField campoNome;
    @FXML
    private TextField campoSenha;

    private Dao<Usuario> dao;

    @FXML
    private void initialize() {
        dao = new Dao<>(Usuario.class);
    }

    @FXML
    private void gravar() {
        if (campoLogin.getText().isBlank() || campoSenha.getText().isBlank()) {
            mostrarErro("Preencha os campos obrigatórios");
            return;
        }

        String login = campoLogin.getText().toUpperCase();
        Usuario temp = dao.buscarPorChave("login", login);

        if (temp != null) {
            mostrarErro("Já existe este login");
            return;
        }

        Usuario usuario = new Usuario(login, campoNome.getText(), campoSenha.getText());
        dao.inserir(usuario);

        limparCampos();
        mostrarSucesso("Usuário cadastrado com sucesso");
    }

    @FXML
    private void cancelar() throws IOException {
        App.setRoot("telaLogin");
    }
    
    private void limparCampos() {
        this.campoLogin.setText("");
        this.campoNome.setText("");
        this.campoSenha.setText("");
    }

    private void mostrarSucesso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.show();
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.show();
    }
}
