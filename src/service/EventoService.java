package service;

import model.Categoria;
import model.Evento;
import util.DataUtils;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class EventoService {
    private List<Evento> eventos = new ArrayList<>();
    private Set<Evento> eventosParticipando = new HashSet<>();
    private final String FILE_PATH = "data/eventos.ser";

    public EventoService() {
        carregarEventos();
    }

    public void cadastrarEvento(Evento evento) {
        eventos.add(evento);
        salvarEventos();
    }

    public void removerEvento(String nomeEvento) {
        eventos.removeIf(e -> e.getNome().equalsIgnoreCase(nomeEvento));
        eventosParticipando.removeIf(e -> e.getNome().equalsIgnoreCase(nomeEvento));
        salvarEventos();
    }

    public void listarEventosOrdenados() {
        eventos.sort(Comparator.comparing(Evento::getHorario));
        for (Evento e : eventos) {
            System.out.println(e.getNome() + " - " + DataUtils.formatarData(e.getHorario()) + " - " + formatarCategoria(e.getCategoria()));
        }
    }

    public void confirmarParticipacao(String nomeEvento) {
        Evento evento = buscarEventoPorNome(nomeEvento);
        if (evento != null) {
            eventosParticipando.add(evento);
            System.out.println("Participação confirmada em: " + evento.getNome());
        } else {
            System.out.println("Evento não encontrado.");
        }
    }

    public void cancelarParticipacao(String nomeEvento) {
        eventosParticipando.removeIf(e -> e.getNome().equalsIgnoreCase(nomeEvento));
    }

    public void listarParticipacoes() {
        for (Evento e : eventosParticipando) {
            System.out.println(e.getNome() + " - " + DataUtils.formatarData(e.getHorario()) + " - " + formatarCategoria(e.getCategoria()));
        }
    }

    public void verificarEventosAtuais() {
        LocalDateTime agora = LocalDateTime.now();
        for (Evento e : eventos) {
            if (e.getHorario().isBefore(agora.plusMinutes(1)) && e.getHorario().isAfter(agora.minusMinutes(1))) {
                System.out.println("Evento ocorrendo agora: " + e);
            }
        }
    }

    public void eventosPassados() {
        LocalDateTime agora = LocalDateTime.now();
        for (Evento e : eventos) {
            if (e.getHorario().isBefore(agora)) {
                System.out.println("Já ocorreu: " + e);
            }
        }
    }

    public Evento buscarEventoPorNome(String nome) {
        return eventos.stream()
                .filter(e -> e.getNome().equalsIgnoreCase(nome))
                .findFirst()
                .orElse(null);
    }

    private void salvarEventos() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(eventos);
        } catch (IOException e) {
            System.out.println("Erro ao salvar eventos: " + e.getMessage());
        }
    }

    private void carregarEventos() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            eventos = (List<Evento>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro ao carregar eventos: " + e.getMessage());
        }
    }

    private String formatarCategoria(Categoria categoria) {
        return categoria.name().charAt(0) + categoria.name().substring(1).toLowerCase();
    }
}
