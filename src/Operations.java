package src;

import java.sql.*;
import java.util.Scanner;

public class Operations {
    private static Scanner scanner = new Scanner(System.in);
    
    public static void lerOperacao(Connection conn) {
        System.out.println("\n=== OPERAÇÕES DISPONÍVEIS ===");
        System.out.println("1. Inserir novo usuário");
        System.out.println("2. Inserir novo perfil");
        System.out.println("3. Inserir nova publicação");
        System.out.println("4. Atualizar usuário");
        System.out.println("5. Atualizar perfil");
        System.out.println("6. Deletar usuário");
        System.out.println("7. Deletar perfil");
        System.out.println("8. Deletar publicação");
        System.out.println("9. Listar usuários");
        System.out.println("10. Listar perfis");
        System.out.println("11. Listar publicações");
        System.out.println("0. Voltar");
        
        System.out.print("\nEscolha uma operação: ");
        int opcao = scanner.nextInt();
        scanner.nextLine(); // Consumir quebra de linha
        
        try {
            switch (opcao) {
                case 1 -> inserirUsuario(conn);
                case 2 -> inserirPerfil(conn);
                case 3 -> inserirPublicacao(conn);
                case 4 -> atualizarUsuario(conn);
                case 5 -> atualizarPerfil(conn);
                case 6 -> deletarUsuario(conn);
                case 7 -> deletarPerfil(conn);
                case 8 -> deletarPublicacao(conn);
                case 9 -> listarUsuarios(conn);
                case 10 -> listarPerfis(conn);
                case 11 -> listarPublicacoes(conn);
                case 0 -> System.out.println("Voltando...");
                default -> System.out.println("Opção inválida!");
            }
        } catch (SQLException e) {
            System.out.println("Erro na operação: " + e.getMessage());
        }
    }
    
    // === OPERAÇÕES DE INSERT ===
    
    public static void inserirUsuario(Connection conn) throws SQLException {
        System.out.println("\n=== INSERIR NOVO USUÁRIO ===");
        
        System.out.print("Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Senha: ");
        String senha = scanner.nextLine();
        
        System.out.print("Deseja adicionar endereço? (s/n): ");
        boolean adicionarEndereco = scanner.nextLine().toLowerCase().startsWith("s");
        
        Integer cep = null, numero = null;
        String rua = null, cidade = null;
        
        if (adicionarEndereco) {
            System.out.print("CEP (8 dígitos): ");
            cep = scanner.nextInt();
            scanner.nextLine();
            
            System.out.print("Número: ");
            numero = scanner.nextInt();
            scanner.nextLine();
            
            System.out.print("Rua: ");
            rua = scanner.nextLine();
            
            System.out.print("Cidade: ");
            cidade = scanner.nextLine();
        }
        
        String sql = "INSERT INTO usuarios (email, senha, cep, numero, rua, cidade) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, senha);
            pstmt.setObject(3, cep);
            pstmt.setObject(4, numero);
            pstmt.setString(5, rua);
            pstmt.setString(6, cidade);
            
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Usuário inserido com sucesso! (" + rowsAffected + " linha afetada)");
        }
        catch (SQLException e) {
            System.out.println("Erro ao inserir usuário: " + e.getMessage());
        }
    }
    
    public static void inserirPerfil(Connection conn) throws SQLException {
        System.out.println("\n=== INSERIR NOVO PERFIL ===");
        
        // Listar usuários disponíveis
        System.out.println("Usuários disponíveis:");
        listarUsuarios(conn);
        
        System.out.print("ID do usuário: ");
        int idUsuario = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Username: ");
        String username = scanner.nextLine();
        
        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();
        
        System.out.print("Perfil privado? (s/n): ");
        boolean privado = scanner.nextLine().toLowerCase().startsWith("s");
        
        System.out.print("Perfil verificado? (s/n): ");
        boolean verificado = scanner.nextLine().toLowerCase().startsWith("s");
        
        String sql = "INSERT INTO perfis (username, descricao, privado, verificado, id_usuario) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, descricao);
            pstmt.setBoolean(3, privado);
            pstmt.setBoolean(4, verificado);
            pstmt.setInt(5, idUsuario);
            
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Perfil inserido com sucesso! (" + rowsAffected + " linha afetada)");
        }
    }
    
    public static void inserirPublicacao(Connection conn) throws SQLException {
        System.out.println("\n=== INSERIR NOVA PUBLICAÇÃO ===");
        
        // Listar perfis disponíveis
        System.out.println("Perfis disponíveis:");
        listarPerfis(conn);
        
        System.out.print("ID do perfil: ");
        int idPerfil = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Data de postagem (YYYY-MM-DD HH:MM:SS): ");
        String dataPostagem = scanner.nextLine();
        
        System.out.print("Data de realização (YYYY-MM-DD HH:MM:SS): ");
        String dataRealizacao = scanner.nextLine();
        
        System.out.print("Legenda: ");
        String legenda = scanner.nextLine();
        
        System.out.print("Tipo (post/story): ");
        String tipo = scanner.nextLine();
        
        String sql = "INSERT INTO publicacoes (data_postagem, data_realizacao, legenda, id_perfil, tipo) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dataPostagem);
            pstmt.setString(2, dataRealizacao);
            pstmt.setString(3, legenda);
            pstmt.setInt(4, idPerfil);
            pstmt.setString(5, tipo);
            
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Publicação inserida com sucesso! (" + rowsAffected + " linha afetada)");
        }
    }
    
    // === OPERAÇÕES DE UPDATE ===
    
    public static void atualizarUsuario(Connection conn) throws SQLException {
        System.out.println("\n=== ATUALIZAR USUÁRIO ===");
        
        // Listar usuários
        listarUsuarios(conn);
        
        System.out.print("ID do usuário a atualizar: ");
        int idUsuario = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Novo email: ");
        String email = scanner.nextLine();
        
        System.out.print("Nova senha: ");
        String senha = scanner.nextLine();
        
        System.out.print("Deseja atualizar endereço? (s/n): ");
        boolean atualizarEndereco = scanner.nextLine().toLowerCase().startsWith("s");
        
        String sql;
        if (atualizarEndereco) {
            System.out.print("Novo CEP (8 dígitos): ");
            int cep = scanner.nextInt();
            scanner.nextLine();
            
            System.out.print("Novo número: ");
            int numero = scanner.nextInt();
            scanner.nextLine();
            
            System.out.print("Nova rua: ");
            String rua = scanner.nextLine();
            
            System.out.print("Nova cidade: ");
            String cidade = scanner.nextLine();
            
            sql = "UPDATE usuarios SET email = ?, senha = ?, cep = ?, numero = ?, rua = ?, cidade = ? WHERE id_usuario = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, email);
                pstmt.setString(2, senha);
                pstmt.setInt(3, cep);
                pstmt.setInt(4, numero);
                pstmt.setString(5, rua);
                pstmt.setString(6, cidade);
                pstmt.setInt(7, idUsuario);
                
                int rowsAffected = pstmt.executeUpdate();
                System.out.println("Usuário atualizado com sucesso! (" + rowsAffected + " linha afetada)");
            }
        } else {
            sql = "UPDATE usuarios SET email = ?, senha = ? WHERE id_usuario = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, email);
                pstmt.setString(2, senha);
                pstmt.setInt(3, idUsuario);
                
                int rowsAffected = pstmt.executeUpdate();
                System.out.println("Usuário atualizado com sucesso! (" + rowsAffected + " linha afetada)");
            }
            catch (SQLException e) {
                System.out.println("Erro ao atualizar usuário: " + e.getMessage());
            }
        }
    }
    
    public static void atualizarPerfil(Connection conn) throws SQLException {
        System.out.println("\n=== ATUALIZAR PERFIL ===");
        
        // Listar perfis
        listarPerfis(conn);
        
        System.out.print("ID do perfil a atualizar: ");
        int idPerfil = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Novo username: ");
        String username = scanner.nextLine();
        
        System.out.print("Nova descrição: ");
        String descricao = scanner.nextLine();
        
        System.out.print("Perfil privado? (s/n): ");
        boolean privado = scanner.nextLine().toLowerCase().startsWith("s");
        
        System.out.print("Perfil verificado? (s/n): ");
        boolean verificado = scanner.nextLine().toLowerCase().startsWith("s");
        
        String sql = "UPDATE perfis SET username = ?, descricao = ?, privado = ?, verificado = ? WHERE id_perfil = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, descricao);
            pstmt.setBoolean(3, privado);
            pstmt.setBoolean(4, verificado);
            pstmt.setInt(5, idPerfil);
            
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Perfil atualizado com sucesso! (" + rowsAffected + " linha afetada)");
        }
    }
    
    // === OPERAÇÕES DE DELETE ===
    
    public static void deletarUsuario(Connection conn) throws SQLException {
        System.out.println("\n=== DELETAR USUÁRIO ===");
        
        // Listar usuários
        listarUsuarios(conn);
        
        System.out.print("ID do usuário a deletar: ");
        int idUsuario = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Tem certeza que deseja deletar este usuário? (s/n): ");
        if (!scanner.nextLine().toLowerCase().startsWith("s")) {
            System.out.println("Operação cancelada.");
            return;
        }
        
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Usuário deletado com sucesso! (" + rowsAffected + " linha afetada)");
            } else {
                System.out.println("Nenhum usuário encontrado com esse ID.");
            }
        }
    }
    
    public static void deletarPerfil(Connection conn) throws SQLException {
        System.out.println("\n=== DELETAR PERFIL ===");
        
        // Listar perfis
        listarPerfis(conn);
        
        System.out.print("ID do perfil a deletar: ");
        int idPerfil = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Tem certeza que deseja deletar este perfil? (s/n): ");
        if (!scanner.nextLine().toLowerCase().startsWith("s")) {
            System.out.println("Operação cancelada.");
            return;
        }
        
        String sql = "DELETE FROM perfis WHERE id_perfil = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPerfil);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Perfil deletado com sucesso! (" + rowsAffected + " linha afetada)");
            } else {
                System.out.println("Nenhum perfil encontrado com esse ID.");
            }
        }
    }
    
    public static void deletarPublicacao(Connection conn) throws SQLException {
        System.out.println("\n=== DELETAR PUBLICAÇÃO ===");
        
        // Listar publicações
        listarPublicacoes(conn);
        
        System.out.print("ID da publicação a deletar: ");
        int idPublicacao = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Tem certeza que deseja deletar esta publicação? (s/n): ");
        if (!scanner.nextLine().toLowerCase().startsWith("s")) {
            System.out.println("Operação cancelada.");
            return;
        }
        
        String sql = "DELETE FROM publicacoes WHERE id_publicacao = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPublicacao);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Publicação deletada com sucesso! (" + rowsAffected + " linha afetada)");
            } else {
                System.out.println("Nenhuma publicação encontrada com esse ID.");
            }
        }
    }
    
    // === OPERAÇÕES DE SELECT ===
    
    public static void listarUsuarios(Connection conn) throws SQLException {
        String sql = "SELECT id_usuario, email, cep, numero, rua, cidade FROM usuarios";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println("\n--- USUÁRIOS ---");
            System.out.printf("%-5s %-25s %-10s %-8s %-20s %-15s%n", 
                "ID", "Email", "CEP", "Número", "Rua", "Cidade");
            System.out.println("-".repeat(80));
            
            while (rs.next()) {
                System.out.printf("%-5d %-25s %-10s %-8s %-20s %-15s%n",
                    rs.getInt("id_usuario"),
                    rs.getString("email"),
                    rs.getObject("cep") != null ? rs.getString("cep") : "N/A",
                    rs.getObject("numero") != null ? rs.getString("numero") : "N/A",
                    rs.getString("rua") != null ? rs.getString("rua") : "N/A",
                    rs.getString("cidade") != null ? rs.getString("cidade") : "N/A"
                );
            }
        }
    }
    
    public static void listarPerfis(Connection conn) throws SQLException {
        String sql = """
            SELECT p.id_perfil, p.username, p.descricao, p.privado, p.verificado, 
                   u.email as usuario_email
            FROM perfis p 
            JOIN usuarios u ON p.id_usuario = u.id_usuario
            """;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println("\n--- PERFIS ---");
            System.out.printf("%-5s %-15s %-25s %-10s %-10s %-20s%n", 
                "ID", "Username", "Descrição", "Privado", "Verificado", "Email do Usuário");
            System.out.println("-".repeat(85));
            
            while (rs.next()) {
                System.out.printf("%-5d %-15s %-25s %-10s %-10s %-20s%n",
                    rs.getInt("id_perfil"),
                    rs.getString("username"),
                    rs.getString("descricao") != null ? rs.getString("descricao") : "N/A",
                    rs.getBoolean("privado") ? "Sim" : "Não",
                    rs.getBoolean("verificado") ? "Sim" : "Não",
                    rs.getString("usuario_email")
                );
            }
        }
    }
    
    public static void listarPublicacoes(Connection conn) throws SQLException {
        String sql = """
            SELECT p.id_publicacao, p.data_postagem, p.legenda, p.tipo, 
                   pf.username as autor
            FROM publicacoes p 
            JOIN perfis pf ON p.id_perfil = pf.id_perfil
            """;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println("\n--- PUBLICAÇÕES ---");
            System.out.printf("%-5s %-20s %-30s %-8s %-15s%n", 
                "ID", "Data Postagem", "Legenda", "Tipo", "Autor");
            System.out.println("-".repeat(80));
            
            while (rs.next()) {
                System.out.printf("%-5d %-20s %-30s %-8s %-15s%n",
                    rs.getInt("id_publicacao"),
                    rs.getString("data_postagem"),
                    rs.getString("legenda") != null ? 
                        (rs.getString("legenda").length() > 30 ? 
                         rs.getString("legenda").substring(0, 27) + "..." : 
                         rs.getString("legenda")) : "N/A",
                    rs.getString("tipo"),
                    rs.getString("autor")
                );
            }
        }
    }
}
