package manager;

import model.Evento;
import model.Usuario;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class EventoManager {
    private List<Evento> eventos;
    private Map<Usuario, List<Evento>> participacoes;
    private static final String ARQUIVO_EVENTOS = "events.data";

    public EventoManager() {
        eventos = carregarEventosDoArquivo();
        participacoes = new HashMap<>();
    }

    public void cadastrarEvento(Evento evento) {
        eventos.add(evento);
        salvarEventosNoArquivo();
    }

    public List<Evento> listarEventosOrdenados() {
        List<Evento> ordenados = eventos.stream()
                .sorted(Comparator.comparing(Evento::getHorario))
                .collect(Collectors.toList());

        if (ordenados.isEmpty()) {
            System.out.println("Nenhum evento cadastrado.");
        } else {
            for (Evento e : ordenados) {
                System.out.printf("• %s (%s) - %s às %s\n",
                        e.getNome(), e.getCategoria(), e.getEndereco(), e.getHorario().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                System.out.println("  Descrição: " + e.getDescricao());
                System.out.println();

            }
        }
        return ordenados;
    }

    public List<Evento> getEventos() {
        return eventos;
    }

    public void participarEvento(Usuario usuario, Evento evento) {
        participacoes.computeIfAbsent(usuario, k -> new ArrayList<>()).add(evento);
    }

    public void cancelarParticipacao(Usuario usuario, Evento evento) {
        List<Evento> lista = participacoes.get(usuario);
        if (lista != null) {
            lista.remove(evento);
        }
    }

    public void listarEventosConfirmados(Usuario usuario) {
        List<Evento> confirmados = participacoes.getOrDefault(usuario, new ArrayList<>());

        if (confirmados.isEmpty()) {
            System.out.println("Você ainda não confirmou participação em eventos.");
        } else {
            System.out.println("Eventos confirmados:");
            for (Evento e : confirmados) {
                System.out.printf("• %s em %s (%s)\n",
                        e.getNome(), e.getEndereco(), e.getHorario().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            }
        }
    }

    public void listarEventosOcorrendoAgora() {
        List<Evento> atuais = eventos.stream()
                .filter(Evento::estaOcorrendoAgora)
                .collect(Collectors.toList());

        if (atuais.isEmpty()) {
            System.out.println("Nenhum evento ocorrendo neste momento.");
        } else {
            System.out.println("Eventos ocorrendo agora:");
            for (Evento e : atuais) {
                System.out.printf("• %s em %s\n", e.getNome(), e.getEndereco());
            }
        }
    }

    public void listarEventosPassados() {
        List<Evento> passados = eventos.stream()
                .filter(Evento::jaOcorreu)
                .collect(Collectors.toList());

        if (passados.isEmpty()) {
            System.out.println("Nenhum evento já ocorreu.");
        } else {
            System.out.println("Eventos já ocorridos:");
            for (Evento e : passados) {
                System.out.printf("• %s em %s (%s)\n",
                        e.getNome(), e.getEndereco(), e.getHorario().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            }
        }
    }

    private void salvarEventosNoArquivo() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_EVENTOS))) {
            oos.writeObject(eventos);
        } catch (IOException e) {
            System.out.println("Erro ao salvar eventos: " + e.getMessage());
        }
    }

    private List<Evento> carregarEventosDoArquivo() {
        File arquivo = new File(ARQUIVO_EVENTOS);
        if (!arquivo.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            return (List<Evento>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro ao carregar eventos: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
