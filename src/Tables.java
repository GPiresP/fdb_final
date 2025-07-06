package src;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Tables {
    public static void criarTabelasEViews() {

        try (Connection conn = SQLiteConnection.connect();
                Statement stmt = conn.createStatement()) {

            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS usuarios (
                            id_usuario INTEGER PRIMARY KEY,
                            email TEXT NOT NULL,
                            senha TEXT NOT NULL,
                            cep INTEGER,
                            numero INTEGER,
                            rua TEXT,
                            cidade TEXT
                        );
                    """);

            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS telefones (
                            id_telefone INTEGER PRIMARY KEY,
                            telefone TEXT NOT NULL UNIQUE,
                            id_usuario INTEGER NOT NULL,
                            FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
                        );
                    """);

            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS perfis (
                            id_perfil INTEGER PRIMARY KEY,
                            username TEXT NOT NULL UNIQUE,
                            descricao TEXT,
                            foto_perfil BLOB,
                            privado BOOLEAN NOT NULL,
                            verificado BOOLEAN NOT NULL,
                            id_usuario INTEGER NOT NULL,
                            FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
                        );
                    """);

            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS publicacoes (
                            id_publicacao INTEGER PRIMARY KEY,
                            data_postagem TEXT NOT NULL,
                            data_realizacao TEXT NOT NULL,
                            legenda TEXT,
                            id_perfil INTEGER NOT NULL,
                            posicao INTEGER,
                            data_fim TEXT,
                            tipo TEXT NOT NULL, -- 'post' ou 'story'
                            FOREIGN KEY (id_perfil) REFERENCES perfis(id_perfil)
                        );
                    """);

            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS conteudos (
                            id_conteudo INTEGER PRIMARY KEY,
                            ordem INTEGER NOT NULL,
                            tamanho INTEGER NOT NULL,
                            tipo TEXT NOT NULL, -- 'foto' ou 'video'
                            formato TEXT, -- 'quadrado' ou 'retangular'
                            duracao INTEGER,
                            conteudo BLOB NOT NULL,
                            id_publicacao INTEGER NOT NULL,
                            FOREIGN KEY (id_publicacao) REFERENCES publicacoes(id_publicacao)
                        );
                    """);

            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS hashtags (
                            id_hashtag INTEGER PRIMARY KEY,
                            nome TEXT UNIQUE NOT NULL
                        );
                    """);

            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS comentarios (
                            id_comentario INTEGER PRIMARY KEY,
                            texto TEXT NOT NULL,
                            data TEXT NOT NULL,
                            id_perfil INTEGER NOT NULL,
                            id_publicacao INTEGER NOT NULL,
                            FOREIGN KEY (id_perfil) REFERENCES perfis(id_perfil),
                            FOREIGN KEY (id_publicacao) REFERENCES publicacoes(id_publicacao)
                        );
                    """);

            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS destaques (
                            id_destaque INTEGER PRIMARY KEY,
                            nome TEXT NOT NULL,
                            capa BLOB NOT NULL,
                            id_perfil INTEGER NOT NULL,
                            FOREIGN KEY (id_perfil) REFERENCES perfis(id_perfil)
                        );
                    """);

            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS follows (
                            id_seguidor INTEGER NOT NULL,
                            id_seguido INTEGER NOT NULL,
                            data_follow TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            PRIMARY KEY (id_seguidor, id_seguido),
                            FOREIGN KEY (id_seguidor) REFERENCES perfis(id_perfil),
                            FOREIGN KEY (id_seguido) REFERENCES perfis(id_perfil)
                        );
                    """);

            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS curtidas (
                            id_perfil INTEGER NOT NULL,
                            id_publicacao INTEGER NOT NULL,
                            data_curtida TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            PRIMARY KEY (id_perfil, id_publicacao),
                            FOREIGN KEY (id_perfil) REFERENCES perfis(id_perfil),
                            FOREIGN KEY (id_publicacao) REFERENCES publicacoes(id_publicacao)
                        );
                    """);

            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS categorizacoes (
                            id_hashtag INTEGER NOT NULL,
                            id_publicacao INTEGER NOT NULL,
                            PRIMARY KEY (id_hashtag, id_publicacao),
                            FOREIGN KEY (id_hashtag) REFERENCES hashtags(id_hashtag),
                            FOREIGN KEY (id_publicacao) REFERENCES publicacoes(id_publicacao)
                        );
                    """);

            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS agregacoes (
                            id_destaque INTEGER NOT NULL,
                            id_publicacao INTEGER NOT NULL,
                            PRIMARY KEY (id_destaque, id_publicacao),
                            FOREIGN KEY (id_destaque) REFERENCES destaques(id_destaque),
                            FOREIGN KEY (id_publicacao) REFERENCES publicacoes(id_publicacao)
                        );
                    """);

            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS visualizacoes (
                            id_visualizacao INTEGER PRIMARY KEY,
                            id_perfil INTEGER NOT NULL,
                            id_publicacao INTEGER NOT NULL,
                            data_visualizacao TEXT NOT NULL,
                            FOREIGN KEY (id_perfil) REFERENCES perfis(id_perfil),
                            FOREIGN KEY (id_publicacao) REFERENCES publicacoes(id_publicacao)
                        );
                    """);

            stmt.execute("""
                        CREATE VIEW IF NOT EXISTS vw_publicacoes_autores_likes AS
                        SELECT
                            p.id_publicacao,
                            p.legenda,
                            p.tipo,
                            pf.username AS autor,
                            u.email AS autor_email,
                            COUNT(cu.id_perfil) AS total_likes
                        FROM publicacoes p
                        JOIN perfis pf ON p.id_perfil = pf.id_perfil
                        JOIN usuarios u ON pf.id_usuario = u.id_usuario
                        LEFT JOIN curtidas cu ON p.id_publicacao = cu.id_publicacao
                        GROUP BY p.id_publicacao, p.legenda, p.tipo, pf.username, u.email;
                    """);

            System.out.println("Tabelas e view criadas com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao criar tabelas/views: " + e.getMessage());
        }
    }
}
