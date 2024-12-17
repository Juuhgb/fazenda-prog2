package com;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import modelo.Usuario;
import util.Dao;

public class UsuarioAlterarControle {

    @FXML
    private TextField campoLoginAtual;

    @FXML
    private TextField campoNovoNome;

    @FXML
    private TextField campoNovoLogin;

    @FXML
    private TextField campoNovaSenha;

    private Dao<Usuario> dao;
    private Usuario usuario;

    @FXML
    private void initialize() {
        dao = new Dao<>(Usuario.class);
    }

    @FXML
    private void buscarUsuario() {
        String loginAtual = campoLoginAtual.getText();
        if (loginAtual.isBlank()) {
            mostrarErro("Digite o login atual para buscar.");
            return;
        }

        usuario = dao.buscarPorChave("login", loginAtual.toUpperCase());
        if (usuario == null) {
            mostrarErro("Usuário não encontrado.");
            return;
        }

        // Preenche os campos com os dados do usuário
        campoNovoNome.setText(usuario.getNome());
        campoNovoLogin.setText(usuario.getLogin());
        campoNovaSenha.setText(usuario.getSenha());
    }

    @FXML
    private void salvarAlteracoes() {
        if (usuario == null) {
            mostrarErro("Nenhum usuário foi selecionado para alteração.");
            return;
        }

        String novoNome = campoNovoNome.getText();
        String novoLogin = campoNovoLogin.getText().toUpperCase();
        String novaSenha = campoNovaSenha.getText();

        if (novoNome.isBlank() || novoLogin.isBlank() || novaSenha.isBlank()) {
            mostrarErro("Todos os campos devem ser preenchidos.");
            return;
        }

        // Verificar se o novo login já existe e não pertence ao usuário atual
        Usuario usuarioExistente = dao.buscarPorChave("login", novoLogin);
        if (usuarioExistente != null && !usuarioExistente.getLogin().equals(usuario.getLogin())) {
            mostrarErro("O novo login já está em uso.");
            return;
        }

        // Atualizar o usuário
        usuario.setNome(novoNome);
        usuario.setLogin(novoLogin);
        usuario.setSenha(novaSenha);

        dao.alterar("login", campoLoginAtual.getText().toUpperCase(), usuario);
        mostrarSucesso("Usuário alterado com sucesso.");
        limparCampos();
    }

    @FXML
    private void voltarMenu() throws IOException {
        App.setRoot("menu");
    }

    private void limparCampos() {
        campoLoginAtual.setText("");
        campoNovoNome.setText("");
        campoNovoLogin.setText("");
        campoNovaSenha.setText("");
        usuario = null;
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
