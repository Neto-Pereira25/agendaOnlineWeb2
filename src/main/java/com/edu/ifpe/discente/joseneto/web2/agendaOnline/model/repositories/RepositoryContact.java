package com.edu.ifpe.discente.joseneto.web2.agendaOnline.model.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.edu.ifpe.discente.joseneto.web2.agendaOnline.model.db.DBException;
import com.edu.ifpe.discente.joseneto.web2.agendaOnline.model.db.Database;
import com.edu.ifpe.discente.joseneto.web2.agendaOnline.model.entities.Contact;
import com.edu.ifpe.discente.joseneto.web2.agendaOnline.model.entities.Phone;

public class RepositoryContact implements Repository<Contact, Integer> {

    private final RepositoryPhone repositoryPhone = new RepositoryPhone();

    public RepositoryContact() {
    }

    @Override
    public void insert(Contact c) throws SQLException {
        PreparedStatement pstm = null;

        try {
            String query = """
                    INSERT INTO contato
                        (nome, email, endereco, usuario_id)
                    VALUES
                        (?,?,?,?)
                        """;

            pstm = Database.getConnection().prepareStatement(query);

            pstm.setString(1, c.getName());
            pstm.setString(2, c.getEmail());
            pstm.setString(3, c.getAddress());
            pstm.setInt(4, c.getUserId());

            pstm.executeUpdate();

            // int rowsAffected = pstm.executeUpdate();

            // if (rowsAffected > 0) {
            // ResultSet rs = pstm.getGeneratedKeys();

            // if (rs.next()) {
            // int contactId = rs.getInt("id");
            // c.setId(contactId);

            // List<Phone> phones = new ArrayList<>();
            // Phone phone = new Phone();
            // for (Phone ph : c.getPhones()) {
            // if (ph.getContactId() == contactId) {
            // phone.setContactId(ph.getContactId());
            // phone.setPhoneNumber(ph.getPhoneNumber());
            // phone.setPhoneType(ph.getPhoneType());

            // repositoryPhone.insert(phone);
            // }
            // }
            // }
            // } else {
            // throw new DBException("Unexpected error! No rows affected!");
            // }

        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            Database.closeStatement(pstm);
        }
    }

    @Override
    public void update(Contact c) throws SQLException {
        PreparedStatement pstm = null;

        try {
            String query = """
                    UPDATE contato
                    SET
                        nome = ?,
                        email = ?,
                        endereco = ?,
                        usuario_id = ?
                    WHERE
                        id = ?
                    """;

            pstm = Database.getConnection().prepareStatement(query);

            pstm.setString(1, c.getName());
            pstm.setString(2, c.getEmail());
            pstm.setString(3, c.getAddress());
            pstm.setInt(4, c.getUserId());
            pstm.setInt(5, c.getId());

            pstm.execute();
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            Database.closeStatement(pstm);
        }
    }

    @Override
    public Contact findById(Integer k) throws SQLException {
        String query = """
                SELECT *
                FROM contato
                WHERE id = ?
                """;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            pstm = Database.getConnection().prepareStatement(query);
            pstm.setInt(1, k);
            rs = pstm.executeQuery();

            if (rs.next()) {
                return instantiatedContact(rs);
            }

            return null;
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            Database.closeResultSet(rs);
            Database.closeStatement(pstm);
        }
    }

    public List<Contact> findAll(int usuarioId) throws SQLException {
        String query = """
                SELECT *
                FROM contato
                WHERE usuario_id = ?
                """;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        List<Contact> contacts = new ArrayList<>();

        try {
            pstm = Database.getConnection().prepareStatement(query);
            pstm.setInt(1, usuarioId);

            rs = pstm.executeQuery();

            while (rs.next()) {
                Contact c = instantiatedContact(rs);
                contacts.add(c);
            }

            return contacts;
        } catch (Exception e) {
            throw new DBException(e.getMessage());
        } finally {
            Database.closeResultSet(rs);
            Database.closeStatement(pstm);
        }
    }

    public List<Contact> findAllByName(int usuarioId, String name) {
        String query = """
                SELECT *
                FROM contato
                WHERE usuario_id = ? AND nome LIKE ?
                """;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        List<Contact> contacts = new ArrayList<>();

        try {
            pstm = Database.getConnection().prepareStatement(query);
            pstm.setInt(1, usuarioId);
            pstm.setString(2, "%" + name + "%");

            rs = pstm.executeQuery();

            while (rs.next()) {
                Contact c = instantiatedContact(rs);
                contacts.add(c);
            }

            return contacts;
        } catch (Exception e) {
            throw new DBException(e.getMessage());
        } finally {
            Database.closeResultSet(rs);
            Database.closeStatement(pstm);
        }
    }

    @Override
    public void delete(Integer k) throws SQLException {
        String query = """
                DELETE FROM contato WHERE id = ?
                """;

        PreparedStatement pstm = null;
        try {
            pstm = Database.getConnection().prepareStatement(query);
            pstm.setInt(1, k);

            pstm.execute();
        } catch (Exception e) {
            throw new DBException(e.getMessage());
        } finally {
            Database.closeStatement(pstm);
        }
    }

    private Contact instantiatedContact(ResultSet rs) throws SQLException {
        Contact c = new Contact();
        c.setId(rs.getInt("id"));
        c.setName(rs.getString("nome"));
        c.setEmail(rs.getString("email"));
        c.setUserId(rs.getInt("usuario_id"));
        c.setAddress(rs.getString("endereco"));

        List<Phone> phones = repositoryPhone.findAll(c.getId());
        c.setPhones(phones);

        return c;
    }

}
