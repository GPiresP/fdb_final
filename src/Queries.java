package src;

import java.sql.*;

public class Queries {

    public static void top3PublicacoesMaisLikes(Connection conn) {
        executarConsulta(conn, """
                    SELECT v.id_publicacao, v.legenda, v.autor, v.total_likes,
                           GROUP_CONCAT(h.nome, ', ') AS hashtags
                    FROM vw_publicacoes_autores_likes v
                    JOIN categorizacoes cz ON v.id_publicacao = cz.id_publicacao
                    JOIN hashtags h ON cz.id_hashtag = h.id_hashtag
                    GROUP BY v.id_publicacao, v.legenda, v.autor, v.total_likes
                    ORDER BY v.total_likes DESC
                    LIMIT 3
                """);
    }

    public static void comentariosEmStories(Connection conn) {
        executarConsulta(conn, """
                    SELECT c.texto, c.data, pf.username AS commenter, p.legenda AS story_legenda
                    FROM comentarios c
                    JOIN perfis pf ON c.id_perfil = pf.id_perfil
                    JOIN publicacoes p ON c.id_publicacao = p.id_publicacao
                    WHERE p.tipo = 'story'
                """);
    }

    public static void numPublicacoesPorUsuario(Connection conn) {
        executarConsulta(conn, """
                    SELECT u.email, COUNT(p.id_publicacao) AS num_publicacoes
                    FROM usuarios u
                    JOIN perfis pf ON u.id_usuario = pf.id_usuario
                    JOIN publicacoes p ON pf.id_perfil = p.id_perfil
                    GROUP BY u.email
                    HAVING COUNT(p.id_publicacao) > 1
                """);
    }

    public static void numPublicacoesPorHashtag(Connection conn) {
        executarConsulta(conn, """
                    SELECT h.nome AS hashtag, COUNT(cz.id_publicacao) AS num_publicacoes
                    FROM hashtags h
                    JOIN categorizacoes cz ON h.id_hashtag = cz.id_hashtag
                    JOIN publicacoes p ON cz.id_publicacao = p.id_publicacao
                    GROUP BY h.nome
                """);
    }

    public static void usuariosNuncaComentaram(Connection conn) {
        executarConsulta(conn, """
                    SELECT u.email
                    FROM usuarios u
                    WHERE NOT EXISTS (
                        SELECT 1
                        FROM perfis pf
                        JOIN comentarios c ON pf.id_perfil = c.id_perfil
                        WHERE pf.id_usuario = u.id_usuario
                    )
                """);
    }

    public static void publicacoesViagemENatureza(Connection conn) {
        executarConsulta(conn, """
                    SELECT p.id_publicacao, p.legenda
                    FROM publicacoes p
                    WHERE EXISTS (
                        SELECT 1 FROM categorizacoes cz
                        JOIN hashtags h ON cz.id_hashtag = h.id_hashtag
                        WHERE cz.id_publicacao = p.id_publicacao AND h.nome = 'viagem'
                    )
                    AND EXISTS (
                        SELECT 1 FROM categorizacoes cz
                        JOIN hashtags h ON cz.id_hashtag = h.id_hashtag
                        WHERE cz.id_publicacao = p.id_publicacao AND h.nome = 'natureza'
                    )
                """);
    }

    public static void numSeguidoresEseguindo(Connection conn) {
        executarConsulta(conn, """
                    SELECT pf.username,
                        (SELECT COUNT(*) FROM follows f WHERE f.id_seguido = pf.id_perfil) AS num_followers,
                        (SELECT COUNT(*) FROM follows f WHERE f.id_seguidor = pf.id_perfil) AS num_following
                    FROM perfis pf
                    JOIN usuarios u ON pf.id_usuario = u.id_usuario
                """);
    }

    public static void publicacoesZeroLikes(Connection conn) {
        executarConsulta(conn, """
                    SELECT p.id_publicacao, p.legenda, pf.username AS autor, COUNT(c.id_comentario) AS num_comentarios
                    FROM publicacoes p
                    JOIN perfis pf ON p.id_perfil = pf.id_perfil
                    LEFT JOIN comentarios c ON p.id_publicacao = c.id_publicacao
                    WHERE NOT EXISTS (
                        SELECT 1 FROM curtidas cu WHERE cu.id_publicacao = p.id_publicacao
                    )
                    GROUP BY p.id_publicacao, p.legenda, pf.username
                """);
    }

    public static void numPublicacoesPorDestaque(Connection conn) {
        executarConsulta(conn, """
                    SELECT d.nome AS destaque, COUNT(a.id_publicacao) AS num_publicacoes
                    FROM destaques d
                    JOIN agregacoes a ON d.id_destaque = a.id_destaque
                    JOIN publicacoes p ON a.id_publicacao = p.id_publicacao
                    GROUP BY d.nome
                """);
    }

    public static void publicacoesCom2LikesEHashtag(Connection conn) {
        executarConsulta(conn, """
                    SELECT v.id_publicacao, v.legenda, v.autor, v.total_likes,
                           GROUP_CONCAT(h.nome, ', ') AS hashtags
                    FROM vw_publicacoes_autores_likes v
                    JOIN categorizacoes cz ON v.id_publicacao = cz.id_publicacao
                    JOIN hashtags h ON cz.id_hashtag = h.id_hashtag
                    WHERE v.total_likes >= 2
                    GROUP BY v.id_publicacao, v.legenda, v.autor, v.total_likes
                """);
    }

    public static void comentariosStoriesPorUsuario(Connection conn, String username) {
        String query = """
                    SELECT c.texto, c.data, pf.username AS commenter, p.legenda AS story_legenda
                    FROM comentarios c
                    JOIN perfis pf ON c.id_perfil = pf.id_perfil
                    JOIN publicacoes p ON c.id_publicacao = p.id_publicacao
                    WHERE p.tipo = 'story' AND pf.username = ?
                """;
        executarConsultaComParametro(conn, query, username);
    }

    public static void publicacoesPorHashtag(Connection conn, String hashtag) {
        String query = """
                    SELECT p.id_publicacao, p.legenda
                    FROM publicacoes p
                    JOIN categorizacoes cz ON p.id_publicacao = cz.id_publicacao
                    JOIN hashtags h ON cz.id_hashtag = h.id_hashtag
                    WHERE h.nome = ?
                """;
        executarConsultaComParametro(conn, query, hashtag);
    }

    private static void executarConsulta(Connection conn, String query) {
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData meta = rs.getMetaData();
            int columns = meta.getColumnCount();

            while (rs.next()) {
                for (int i = 1; i <= columns; i++) {
                    System.out.print(meta.getColumnName(i) + ": " + rs.getString(i) + " | ");
                }
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void executarConsultaComParametro(Connection conn, String query, String parametro) {
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, parametro);
            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            int columns = meta.getColumnCount();

            while (rs.next()) {
                for (int i = 1; i <= columns; i++) {
                    System.out.print(meta.getColumnName(i) + ": " + rs.getString(i) + " | ");
                }
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}