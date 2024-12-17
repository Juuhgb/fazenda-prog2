package com;

import java.io.IOException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import modelo.Usuario;
import util.Dao;

public class UsuarioListarControle {

    @FXML
    private TableView<Usuario> tabelaUsuarios;

    @FXML
    private TableColumn<Usuario, String> colunaLogin;

    @FXML
    private TableColumn<Usuario, String> colunaNome;

    @FXML
    private TableColumn<Usuario, String> colunaSenha;

    private Dao<Usuario> dao;

    @FXML
    private void initialize() {
        // Configurações iniciais
        dao = new Dao<>(Usuario.class);

        // Configurar as colunas da tabela
        colunaLogin.setCellValueFactory(new PropertyValueFactory<>("login"));
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaSenha.setCellValueFactory(new PropertyValueFactory<>("senha"));

        // Carregar os dados na tabela
        carregarUsuarios();
    }

    /**
     * Método responsável por carregar os usuários na tabela.
     */
    private void carregarUsuarios() {
        List<Usuario> usuarios = dao.listarTodos(); // Busca todos os usuários do banco
        ObservableList<Usuario> observableUsuarios = FXCollections.observableArrayList(usuarios);
        tabelaUsuarios.setItems(observableUsuarios); // Define os dados na tabela
    }

    /**
     * Retorna ao menu principal.
     * @throws IOException Caso o arquivo FXML do menu não seja encontrado.
     */
    @FXML
    private void voltarMenu() throws IOException {
        App.setRoot("menu");
    }
}
