package src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Insertions {
    public static void inserirDados() {
        try (Connection conn = SQLiteConnection.connect();
                Statement stmt = conn.createStatement()) {

            conn.setAutoCommit(false);

            stmt.executeUpdate("""
                        INSERT INTO usuarios (id_usuario, email, senha, cep, numero, rua, cidade) VALUES
                        (1, 'alice@email.com', 'senha123', 12345678, 100, 'Rua das Flores', 'São Paulo'),
                        (2, 'bob@email.com', 'senha456', 87654321, 200, 'Av. Brasil', 'Rio de Janeiro'),
                        (3, 'carol@email.com', 'senha789', 11223344, 300, 'Rua Verde', 'Belo Horizonte'),
                        (4, 'dan@email.com', 'senha321', 44332211, 400, 'Av. Central', 'Curitiba'),
                        (5, 'eva@email.com', 'senha654', 55555555, 500, 'Rua Azul', 'Porto Alegre'),
                        (6, 'fred@email.com', 'senha987', 66666666, 600, 'Av. das Palmeiras', 'Salvador'),
                        (7, 'gina@email.com', 'senhaabc', 77777777, 700, 'Rua do Sol', 'Fortaleza');
                    """);

            stmt.executeUpdate("""
                        INSERT INTO telefones (id_telefone, telefone, id_usuario) VALUES
                        (1, '11999999999', 1),
                        (2, '21988888888', 2),
                        (3, '31977777777', 3),
                        (4, '41966666666', 4),
                        (5, '51955555555', 5),
                        (6, '71966666666', 6),
                        (7, '85977777777', 7);
                    """);

            stmt.executeUpdate(
                    """
                                INSERT INTO perfis (id_perfil, username, descricao, foto_perfil, privado, verificado, id_usuario) VALUES
                                (1, 'alice', 'Perfil da Alice', NULL, 0, 1, 1),
                                (2, 'bob', 'Perfil do Bob', NULL, 1, 0, 2),
                                (3, 'carol', 'Perfil da Carol', NULL, 0, 0, 3),
                                (4, 'dan', 'Perfil do Dan', NULL, 1, 1, 4),
                                (5, 'eva', 'Perfil da Eva', NULL, 0, 0, 5),
                                (6, 'fred', 'Perfil do Fred', NULL, 0, 0, 6),
                                (7, 'gina', 'Perfil da Gina', NULL, 1, 0, 7);
                            """);

            stmt.executeUpdate("""
                        INSERT INTO hashtags (id_hashtag, nome) VALUES
                        (1, 'natureza'),
                        (2, 'viagem'),
                        (3, 'comida'),
                        (4, 'pets'),
                        (5, 'esporte'),
                        (6, 'arte'),
                        (7, 'música');
                    """);

            String now = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            String tomorrow = LocalDateTime.now().plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            stmt.executeUpdate(String.format(
                    """
                                INSERT INTO publicacoes (id_publicacao, data_postagem, data_realizacao, legenda, id_perfil, posicao, data_fim, tipo) VALUES
                                (1, '%1$s', '%1$s', 'Primeiro post!', 1, 1, NULL, 'post'),
                                (2, '%1$s', '%1$s', 'Primeiro story!', 2, 1, '%2$s', 'story'),
                                (3, '%1$s', '%1$s', 'Almoço delicioso!', 3, 1, NULL, 'post'),
                                (4, '%1$s', '%1$s', 'Meu cachorro brincando', 4, 1, '%2$s', 'story'),
                                (5, '%1$s', '%1$s', 'Viagem inesquecível!', 1, 2, NULL, 'post'),
                                (6, '%1$s', '%1$s', 'Show de música ao vivo!', 5, 1, NULL, 'post'),
                                (7, '%1$s', '%1$s', 'Arte na rua', 6, 1, NULL, 'post'),
                                (8, '%1$s', '%1$s', 'Corrida no parque', 7, 1, NULL, 'post'),
                                (9, '%1$s', '%1$s', 'Story da Gina', 7, 2, '%2$s', 'story'),
                                (10, '%1$s', '%1$s', 'Story do Fred', 6, 2, '%2$s', 'story');
                            """,
                    now, tomorrow));

            PreparedStatement psConteudo = conn.prepareStatement(
                    "INSERT INTO conteudos (id_conteudo, ordem, tamanho, tipo, formato, duracao, conteudo, id_publicacao) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            psConteudo.setInt(1, 1);
            psConteudo.setInt(2, 1);
            psConteudo.setInt(3, 2048);
            psConteudo.setString(4, "foto");
            psConteudo.setString(5, "quadrado");
            psConteudo.setObject(6, null);
            psConteudo.setBytes(7, new byte[] { (byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0 });
            psConteudo.setInt(8, 1);
            psConteudo.executeUpdate();
            psConteudo.setInt(1, 2);
            psConteudo.setInt(2, 1);
            psConteudo.setInt(3, 4096);
            psConteudo.setString(4, "video");
            psConteudo.setObject(5, null);
            psConteudo.setInt(6, 30);
            psConteudo.setBytes(7, new byte[] { 0x00, 0x00, 0x00, 0x20 });
            psConteudo.setInt(8, 2);
            psConteudo.executeUpdate();
            psConteudo.setInt(1, 3);
            psConteudo.setInt(2, 1);
            psConteudo.setInt(3, 1024);
            psConteudo.setString(4, "foto");
            psConteudo.setString(5, "retangular");
            psConteudo.setObject(6, null);
            psConteudo.setBytes(7, new byte[] { (byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE1 });
            psConteudo.setInt(8, 3);
            psConteudo.executeUpdate();
            psConteudo.setInt(1, 4);
            psConteudo.setInt(2, 1);
            psConteudo.setInt(3, 5120);
            psConteudo.setString(4, "video");
            psConteudo.setObject(5, null);
            psConteudo.setInt(6, 45);
            psConteudo.setBytes(7, new byte[] { 0x00, 0x00, 0x00, 0x21 });
            psConteudo.setInt(8, 4);
            psConteudo.executeUpdate();
            psConteudo.setInt(1, 5);
            psConteudo.setInt(2, 2);
            psConteudo.setInt(3, 3072);
            psConteudo.setString(4, "foto");
            psConteudo.setString(5, "quadrado");
            psConteudo.setObject(6, null);
            psConteudo.setBytes(7, new byte[] { (byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE2 });
            psConteudo.setInt(8, 5);
            psConteudo.executeUpdate();
            psConteudo.setInt(1, 6);
            psConteudo.setInt(2, 1);
            psConteudo.setInt(3, 2048);
            psConteudo.setString(4, "foto");
            psConteudo.setString(5, "quadrado");
            psConteudo.setObject(6, null);
            psConteudo.setBytes(7, new byte[] { (byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE3 });
            psConteudo.setInt(8, 6);
            psConteudo.executeUpdate();
            psConteudo.setInt(1, 7);
            psConteudo.setInt(2, 1);
            psConteudo.setInt(3, 4096);
            psConteudo.setString(4, "foto");
            psConteudo.setString(5, "retangular");
            psConteudo.setObject(6, null);
            psConteudo.setBytes(7, new byte[] { (byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE4 });
            psConteudo.setInt(8, 7);
            psConteudo.executeUpdate();
            psConteudo.setInt(1, 8);
            psConteudo.setInt(2, 1);
            psConteudo.setInt(3, 1024);
            psConteudo.setString(4, "foto");
            psConteudo.setString(5, "quadrado");
            psConteudo.setObject(6, null);
            psConteudo.setBytes(7, new byte[] { (byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE5 });
            psConteudo.setInt(8, 8);
            psConteudo.executeUpdate();
            psConteudo.setInt(1, 9);
            psConteudo.setInt(2, 1);
            psConteudo.setInt(3, 2048);
            psConteudo.setString(4, "video");
            psConteudo.setObject(5, null);
            psConteudo.setInt(6, 20);
            psConteudo.setBytes(7, new byte[] { 0x00, 0x00, 0x00, 0x22 });
            psConteudo.setInt(8, 9);
            psConteudo.executeUpdate();
            psConteudo.setInt(1, 10);
            psConteudo.setInt(2, 1);
            psConteudo.setInt(3, 2048);
            psConteudo.setString(4, "video");
            psConteudo.setObject(5, null);
            psConteudo.setInt(6, 15);
            psConteudo.setBytes(7, new byte[] { 0x00, 0x00, 0x00, 0x23 });
            psConteudo.setInt(8, 10);
            psConteudo.executeUpdate();

            // Comentários
            stmt.executeUpdate(String.format("""
                        INSERT INTO comentarios (id_comentario, texto, data, id_perfil, id_publicacao) VALUES
                        (1, 'Muito legal!', '%1$s', 2, 1),
                        (2, 'Adorei!', '%1$s', 1, 2),
                        (3, 'Que delícia!', '%1$s', 1, 3),
                        (4, 'Seu cachorro é fofo!', '%1$s', 3, 4),
                        (5, 'Quero viajar também!', '%1$s', 4, 5),
                        (6, 'Amei o show!',  '%1$s', 6, 6),
                        (7, 'Muito criativo!', '%1$s', 5, 7),
                        (8, 'Quero participar!', '%1$s', 1, 8),
                        (9, 'Adorei o story!', '%1$s', 2, 9),
                        (10, 'Legal demais!', '%1$s', 3, 10);
                    """, now));

            PreparedStatement psDestaque = conn.prepareStatement(
                    "INSERT INTO destaques (id_destaque, nome, capa, id_perfil) VALUES (?, ?, ?, ?)");
            psDestaque.setInt(1, 1);
            psDestaque.setString(2, "Viagens");
            psDestaque.setBytes(3, new byte[] { (byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0 });
            psDestaque.setInt(4, 1);
            psDestaque.executeUpdate();
            psDestaque.setInt(1, 2);
            psDestaque.setString(2, "Comidas");
            psDestaque.setBytes(3, new byte[] { (byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE1 });
            psDestaque.setInt(4, 3);
            psDestaque.executeUpdate();
            psDestaque.setInt(1, 3);
            psDestaque.setString(2, "Pets");
            psDestaque.setBytes(3, new byte[] { (byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE2 });
            psDestaque.setInt(4, 4);
            psDestaque.executeUpdate();
            psDestaque.setInt(1, 4);
            psDestaque.setString(2, "Música");
            psDestaque.setBytes(3, new byte[] { (byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE3 });
            psDestaque.setInt(4, 5);
            psDestaque.executeUpdate();
            psDestaque.setInt(1, 5);
            psDestaque.setString(2, "Esportes");
            psDestaque.setBytes(3, new byte[] { (byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE4 });
            psDestaque.setInt(4, 7);
            psDestaque.executeUpdate();

            stmt.executeUpdate("""
                        INSERT INTO follows (id_seguidor, id_seguido) VALUES
                        (1, 2),
                        (2, 1),
                        (3, 1),
                        (4, 2),
                        (3, 4),
                        (5, 1),
                        (6, 2),
                        (7, 3),
                        (5, 6),
                        (6, 7),
                        (7, 5);
                    """);

            stmt.executeUpdate("""
                        INSERT INTO curtidas (id_perfil, id_publicacao) VALUES
                        (1, 2),
                        (2, 1),
                        (3, 3),
                        (1, 5),
                        (2, 6),
                        (3, 6),
                        (4, 6),
                        (5, 6),
                        (6, 7),
                        (7, 7),
                        (1, 8),
                        (2, 8),
                        (3, 8),
                        (4, 9),
                        (5, 9),
                        (6, 10);
                    """);

            stmt.executeUpdate("""
                        INSERT INTO categorizacoes (id_hashtag, id_publicacao) VALUES
                        (1, 1),
                        (2, 2),
                        (3, 3),
                        (4, 4),
                        (2, 5),
                        (5, 8),
                        (6, 7),
                        (7, 6),
                        (1, 6),
                        (2, 6),
                        (1, 7),
                        (2, 7);
                    """);

            stmt.executeUpdate("""
                        INSERT INTO agregacoes (id_destaque, id_publicacao) VALUES
                        (1, 2),
                        (2, 3),
                        (3, 4),
                        (4, 6),
                        (5, 8);
                    """);

            stmt.executeUpdate(String.format("""
                        INSERT INTO visualizacoes (id_visualizacao, id_perfil, id_publicacao, data_visualizacao) VALUES
                        (1, 1, 2, '%1$s'),
                        (2, 2, 1, '%1$s'),
                        (3, 3, 4, '%1$s'),
                        (4, 4, 3, '%1$s'),
                        (5, 2, 5, '%1$s'),
                        (6, 5, 6, '%1$s'),
                        (7, 6, 7, '%1$s'),
                        (8, 7, 8, '%1$s'),
                        (9, 5, 9, '%1$s'),
                        (10, 6, 10, '%1$s');
                    """, now));

            conn.commit();
            System.out.println("Dados inseridos com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao inserir dados: " + e.getMessage());
        }
    }
}
