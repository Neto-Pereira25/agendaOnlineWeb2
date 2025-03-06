package com.edu.ifpe.discente.joseneto.web2.agendaOnline.model.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.edu.ifpe.discente.joseneto.web2.agendaOnline.model.db.DBException;
import com.edu.ifpe.discente.joseneto.web2.agendaOnline.model.db.Database;
import com.edu.ifpe.discente.joseneto.web2.agendaOnline.model.entities.User;

public class RepositoryUser implements Repository<User, Integer> {

    public RepositoryUser() {
    }

    @Override
    public void insert(User u) throws SQLException {
        PreparedStatement pstm = null;

        try {
            String query = """
                    INSERT INTO usuario
                        (nome, email, senha)
                    VALUES
                        (?,?,?)
                        """;

            pstm = Database.getConnection().prepareStatement(query);

            pstm.setString(1, u.getName());
            pstm.setString(2, u.getEmail());
            pstm.setString(3, u.getSenha());

            pstm.execute();
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            Database.closeStatement(pstm);
            Database.closeConnection();
        }
    }

    @Override
    public void update(User u) throws SQLException {
        PreparedStatement pstm = null;

        try {
            String query = """
                    UPDATE usuario
                    SET
                        nome = ?,
                        email = ?,
                        senha = ?
                    WHERE
                        id = ?
                    """;

            pstm = Database.getConnection().prepareStatement(query);

            pstm.setString(1, u.getName());
            pstm.setString(2, u.getEmail());
            pstm.setString(3, u.getSenha());
            pstm.setInt(4, u.getId());

            pstm.execute();
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            Database.closeStatement(pstm);
            Database.closeConnection();
        }
    }

    @Override
    public User findById(Integer k) throws SQLException {
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            String query = String.format("""
                    SELECT *
                    FROM usuario
                    WHERE id = %d
                    """, k);

            pstm = Database.getConnection().prepareStatement(query);
            rs = pstm.executeQuery();

            if (rs.next()) {
                return instantiatedUser(rs);
            }

            return null;
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            Database.closeResultSet(rs);
            Database.closeStatement(pstm);
            Database.closeConnection();
        }
    }

    @Override
    public List<User> findAll() throws SQLException {
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            String query = """
                    SELECT * FROM usuario
                    """;

            pstm = Database.getConnection().prepareStatement(query);
            rs = pstm.executeQuery();

            List<User> users = new ArrayList<>();

            while (rs.next()) {
                User user = instantiatedUser(rs);
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            Database.closeResultSet(rs);
            Database.closeStatement(pstm);
            Database.closeConnection();
        }
    }

    @Override
    public void delete(Integer k) throws SQLException {
        PreparedStatement pstm = null;
        try {
            String query = """
                    DELETE FROM usuario
                    WHERE id = ?
                    """;

            pstm = Database.getConnection().prepareStatement(query);
            pstm.setInt(1, k);
            pstm.executeUpdate();

        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            Database.closeStatement(pstm);
        }
    }

    private User instantiatedUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("nome"));
        user.setEmail(rs.getString("email"));
        user.setSenha(rs.getString("senha"));
        return user;
    }

}
