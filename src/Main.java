package src;

import java.sql.Connection;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Tables.criarTabelasEViews();
        Insertions.inserirDados();

        try (Connection conn = SQLiteConnection.connect()) {
            if (conn == null)
                return;

            while (true) {
                System.out.println("\nEscolha uma opção:");
                System.out.println("1 - Top 3 publicações com mais likes");
                System.out.println("2 - Comentários em stories");
                System.out.println("3 - Número de publicações por usuários com mais de 1 publicação");
                System.out.println("4 - Número de publicações por hashtag");
                System.out.println("5 - Usuários que nunca comentaram");
                System.out.println("6 - Publicações com hashtag 'viagem' e 'natureza'");
                System.out.println("7 - Número de seguidores e perfis seguidos por usuário");
                System.out.println("8 - Publicações com 0 likes");
                System.out.println("9 - Número de publicações por destaque");
                System.out.println("10 - Publicações com pelo menos 2 likes e 1 hashtag");
                System.out.println("11 - Comentários em stories de um usuário (com parâmetro)");
                System.out.println("12 - Publicações com hashtag específica (com parâmetro)");
                System.out.println("0 - Sair");

                int opcao = Integer.parseInt(scanner.nextLine());
                if (opcao == 0)
                    break;

                switch (opcao) {
                    case 1 -> Queries.top3PublicacoesMaisLikes(conn);
                    case 2 -> Queries.comentariosEmStories(conn);
                    case 3 -> Queries.numPublicacoesPorUsuario(conn);
                    case 4 -> Queries.numPublicacoesPorHashtag(conn);
                    case 5 -> Queries.usuariosNuncaComentaram(conn);
                    case 6 -> Queries.publicacoesViagemENatureza(conn);
                    case 7 -> Queries.numSeguidoresEseguindo(conn);
                    case 8 -> Queries.publicacoesZeroLikes(conn);
                    case 9 -> Queries.numPublicacoesPorDestaque(conn);
                    case 10 -> Queries.publicacoesCom2LikesEHashtag(conn);
                    case 11 -> {
                        System.out.print("Digite o nome do usuário: ");
                        String username = scanner.nextLine();
                        Queries.comentariosStoriesPorUsuario(conn, username);
                    }
                    case 12 -> {
                        System.out.print("Digite o nome da hashtag: ");
                        String hashtag = scanner.nextLine();
                        Queries.publicacoesPorHashtag(conn, hashtag);
                    }
                    default -> System.out.println("Opção inválida");
                }
            }
        } catch (Exception e) {
            System.out.println("Erro geral: " + e.getMessage());
        }
    }
}