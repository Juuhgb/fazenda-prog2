package modelo;

import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.LocalDate;

public class Producao {

    @BsonProperty(value = "brinco")
    private String brinco; // Identificador da vaca

    @BsonProperty(value = "data")
    private LocalDate data; // Data da produção

    @BsonProperty(value = "quantidade")
    private double quantidade; // Quantidade produzida (litros)

    private transient String nome; // Nome da vaca (não persistido)

    public Producao() {
    }

    public Producao(String brinco, LocalDate data, double quantidade) {
        this.brinco = brinco;
        this.data = data;
        this.quantidade = quantidade;
    }

    public String getBrinco() {
        return brinco;
    }

    public void setBrinco(String brinco) {
        this.brinco = brinco;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
