package model;

public class Usuario {
    private String nome;
    private String email;
    private String cidade;

    public Usuario(String nome, String email, String cidade) {
        if (nome == null || email == null || cidade == null ||
                nome.isBlank() || email.isBlank() || cidade.isBlank()) {
            throw new IllegalArgumentException("Nome, email e cidade devem ser preenchidos.");
        }
        this.nome = nome;
        this.email = email;
        this.cidade = cidade;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getCidade() {
        return cidade;
    }

    @Override
    public String toString() {
        return "Usuário:\n" +
                "Nome: " + nome + "\n" +
                "Email: " + email + "\n" +
                "Cidade: " + cidade;
    }
}
