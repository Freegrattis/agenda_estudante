import manager.EventoManager;
import model.Categoria;
import model.Evento;
import model.Usuario;

import java.time.LocalDateTime;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        EventoManager eventoManager = new EventoManager();

        System.out.println("=== Cadastro de Usuário ===");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Cidade: ");
        String cidade = scanner.nextLine();

        Usuario usuario = new Usuario(nome, email, cidade);

        int opcao = -1;
        while (opcao != 0) {
            System.out.println("""
                \n==== MENU ====
                1. Cadastrar evento
                2. Listar todos os eventos
                3. Confirmar participação em evento
                4. Cancelar participação
                5. Ver meus eventos confirmados
                6. Ver eventos ocorrendo agora
                7. Ver eventos passados
                0. Sair
                Escolha uma opção:""");

            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Opção inválida. Digite um número.");
                continue;
            }

            switch (opcao) {
                case 1 -> {
                    System.out.print("Nome do evento: ");
                    String nomeEvento = scanner.nextLine();
                    System.out.print("Endereço: ");
                    String endereco = scanner.nextLine();
                    System.out.print("Categoria (Show, Festa, Esportivo): ");
                    Categoria categoria = escolherCategoria(scanner);
                    System.out.print("Descrição: ");
                    String descricao = scanner.nextLine();

                    int ano = lerInt(scanner, "Ano: ");
                    int mes = lerInt(scanner, "Mês: ");
                    int dia = lerInt(scanner, "Dia: ");
                    int hora = lerInt(scanner, "Hora: ");
                    int minuto = lerInt(scanner, "Minuto: ");

                    LocalDateTime horario = LocalDateTime.of(ano, mes, dia, hora, minuto);
                    if (nomeEvento.isBlank() || endereco.isBlank() || descricao.isBlank()) {
                        System.out.println("Erro: nome, endereço e descrição não podem estar vazios.");
                    } else if (horario.isBefore(LocalDateTime.now())) {
                        System.out.println("Erro: a data/hora do evento deve ser no futuro.");
                    } else {
                        try {
                            Evento novoEvento = new Evento(nomeEvento, endereco, categoria, horario, descricao);
                            eventoManager.cadastrarEvento(novoEvento);
                            System.out.println("Evento cadastrado com sucesso.");
                        } catch (IllegalArgumentException e) {
                            System.out.println("Erro: " + e.getMessage());
                        }
                    }
                }
                case 2 -> eventoManager.listarEventosOrdenados();
                case 3 -> {
                    eventoManager.listarEventosOrdenados();
                    System.out.print("Digite o nome exato do evento que deseja participar: ");
                    String nomeEvento = scanner.nextLine();
                    eventoManager.getEventos().stream()
                            .filter(ev -> ev.getNome().equalsIgnoreCase(nomeEvento))
                            .findFirst()
                            .ifPresentOrElse(
                                    ev -> {
                                        eventoManager.participarEvento(usuario, ev);
                                        System.out.println("Participação confirmada!");
                                    },
                                    () -> System.out.println("Evento não encontrado.")
                            );
                }
                case 4 -> {
                    eventoManager.listarEventosConfirmados(usuario);
                    System.out.print("Digite o nome do evento que deseja cancelar: ");
                    String nomeEvento = scanner.nextLine();
                    eventoManager.getEventos().stream()
                            .filter(ev -> ev.getNome().equalsIgnoreCase(nomeEvento))
                            .findFirst()
                            .ifPresent(ev -> {
                                eventoManager.cancelarParticipacao(usuario, ev);
                                System.out.println("Participação cancelada.");
                            });
                }
                case 5 -> eventoManager.listarEventosConfirmados(usuario);
                case 6 -> eventoManager.listarEventosOcorrendoAgora();
                case 7 -> eventoManager.listarEventosPassados();
                case 0 -> System.out.println("Encerrando...");
                default -> System.out.println("Opção inválida.");
            }
        }

        scanner.close();
    }

    private static int lerInt(Scanner scanner, String mensagem) {
        int valor;
        while (true) {
            System.out.print(mensagem);
            try {
                valor = Integer.parseInt(scanner.nextLine());
                return valor;
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Digite um número.");
            }
        }
    }

    private static Categoria escolherCategoria(Scanner scanner) {
        System.out.println("Escolha a categoria:");
        Categoria[] categorias = Categoria.values();
        for (int i = 0; i < categorias.length; i++) {
            System.out.println((i + 1) + " - " + categorias[i].formatado());
        }

        int escolha;
        while (true) {
            try {
                System.out.print("Digite o número correspondente à categoria: ");
                escolha = Integer.parseInt(scanner.nextLine());
                if (escolha >= 1 && escolha <= categorias.length) {
                    return categorias[escolha - 1];
                } else {
                    System.out.println("Escolha inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Digite um número válido.");
            }
        }
    }
}
