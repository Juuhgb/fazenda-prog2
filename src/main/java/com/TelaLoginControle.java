package com;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import modelo.Usuario;
import util.Dao;

public class TelaLoginControle {

    @FXML
    private TextField campoLogin;
    @FXML
    private TextField campoSenha;

    private Dao<Usuario> dao;

    @FXML
    private void initialize() {
        dao = new Dao<>(Usuario.class);
    }

    @FXML
    private void entrar() {
        if (campoLogin.getText().isBlank() || campoSenha.getText().isBlank()) {
            mostrarErro("Preencha os campos obrigatórios");
            return;
        }

        String login = campoLogin.getText().toUpperCase();
        Usuario usuario = dao.buscarPorChave("login", login);

        if (usuario == null || !usuario.getSenha().equals(campoSenha.getText())) {
            mostrarErro("Login ou senha inválidos");
            return;
        }

        try {
            App.setRoot("menu");
        } catch (IOException e) {
            e.printStackTrace();
            mostrarErro("Erro ao carregar o menu");
        }
    }

    @FXML
    private void cadastrar() throws IOException {
        App.setRoot("usuarioIncluir"); // Ir para a tela de cadastro de usuários
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.show();
    }
}
