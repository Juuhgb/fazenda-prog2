package com;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import modelo.Usuario;
import util.Dao;

public class UsuarioExcluirControle {

    @FXML
    private TextField campoLogin;

    private Dao<Usuario> dao;

    @FXML
    private void initialize() {
        dao = new Dao<>(Usuario.class);
    }

    @FXML
    private void excluirUsuario() {
        String login = campoLogin.getText().toUpperCase();

        if (login.isBlank()) {
            mostrarErro("O campo 'Login do Usuário' deve ser preenchido.");
            return;
        }

        // Buscar o usuário pelo login para confirmar que ele existe
        Usuario usuario = dao.buscarPorChave("login", login);

        if (usuario == null) {
            mostrarErro("Nenhum usuário encontrado com o login informado.");
            return;
        }

        // Confirmar a exclusão do usuário
        boolean sucesso = dao.excluir("login", login);

        if (sucesso) {
            mostrarSucesso("Usuário excluído com sucesso.");
            limparCampo();
        } else {
            mostrarErro("Erro ao excluir o usuário. Tente novamente.");
        }
    }

    @FXML
    private void voltarMenu() throws IOException {
        App.setRoot("menu");
    }

    private void limparCampo() {
        campoLogin.setText("");
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
