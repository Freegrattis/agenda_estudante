package model;

public enum Categoria {
    FESTA,
    SHOW,
    ESPORTE,
    TEATRO,
    CONFERENCIA,
    OUTROS;

    public String formatado() {
        String nome = this.name().toLowerCase();
        return Character.toUpperCase(nome.charAt(0)) + nome.substring(1);
    }
}
