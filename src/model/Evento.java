package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Evento implements Serializable {
    private String nome;
    private String endereco;
    private Categoria categoria;
    private LocalDateTime horario;
    private String descricao;

    public Evento(String nome, String endereco, Categoria categoria, LocalDateTime horario, String descricao) {
        if (nome.isBlank() || endereco.isBlank() || descricao.isBlank()) {
            throw new IllegalArgumentException("Todos os campos do evento devem ser preenchidos.");
        }
        this.nome = nome;
        this.endereco = endereco;
        this.categoria = categoria;
        this.horario = horario;
        this.descricao = descricao;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public LocalDateTime getHorario() {
        return horario;
    }

    public String getDescricao() {
        return descricao;
    }

    public boolean estaOcorrendoAgora() {
        LocalDateTime agora = LocalDateTime.now();
        return horario.isAfter(agora.minusMinutes(1)) && horario.isBefore(agora.plusMinutes(1));
    }

    public boolean jaOcorreu() {
        return horario.isBefore(LocalDateTime.now());
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return "\nEvento: " + nome +
                "\nEndereço: " + endereco +
                "\nCategoria: " + categoria +
                "\nHorário: " + horario.format(formatter) +
                "\nDescrição: " + descricao + "\n";
    }
}
