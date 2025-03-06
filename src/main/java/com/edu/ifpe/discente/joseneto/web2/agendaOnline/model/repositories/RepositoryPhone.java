package com.edu.ifpe.discente.joseneto.web2.agendaOnline.model.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.edu.ifpe.discente.joseneto.web2.agendaOnline.model.db.DBException;
import com.edu.ifpe.discente.joseneto.web2.agendaOnline.model.db.Database;
import com.edu.ifpe.discente.joseneto.web2.agendaOnline.model.entities.Phone;

public class RepositoryPhone implements Repository<Phone, Integer> {

    @Override
    public void insert(Phone ph) throws SQLException {
        String query = """
                INSERT INTO telefone
                    (contato_id, numero, tipo)
                VALUES
                    (?,?,?)
                """;

        PreparedStatement pstm = null;
        try {
            pstm = Database.getConnection().prepareStatement(query);

            pstm.setInt(1, ph.getContactId());
            pstm.setString(2, ph.getPhoneNumber());
            pstm.setString(3, ph.getPhoneType());

            pstm.executeUpdate();

        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            Database.closeStatement(pstm);
        }
    }

    @Override
    public void update(Phone ph) throws SQLException {
        String query = """
                UPDATE telefone
                SET
                    contato_id = ?,
                    numero = ?,
                    tipo = ?
                WHERE id = ?
                """;

        PreparedStatement pstm = null;
        try {
            pstm = Database.getConnection().prepareStatement(query);

            pstm.setInt(1, ph.getContactId());
            pstm.setString(2, ph.getPhoneNumber());
            pstm.setString(3, ph.getPhoneType());
            pstm.setInt(4, ph.getId());

            pstm.executeUpdate();

        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        }
    }

    @Override
    public Phone findById(Integer k) throws SQLException {
        String query = String.format("""
                SELECT *
                FROM telefone
                WHERE id = %d
                """, k);
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            pstm = Database.getConnection().prepareStatement(query);

            rs = pstm.executeQuery();

            if (rs.next()) {
                return instantiatedPhone(rs);
            }

            return null;
        } catch (Exception e) {
            throw new DBException(e.getMessage());
        } finally {
            Database.closeResultSet(rs);
            Database.closeStatement(pstm);
        }
    }

    public List<Phone> findAll(int contactId) throws SQLException {
        String query = """
                SELECT *
                FROM telefone
                WHERE contato_id = ?
                """;

        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<Phone> phones = new ArrayList<>();

        try {
            pstm = Database.getConnection().prepareStatement(query);

            pstm.setInt(1, contactId);

            rs = pstm.executeQuery();

            while (rs.next()) {
                Phone phone = new Phone();
                phone.setId(rs.getInt("id"));
                phone.setContactId(rs.getInt("contato_id"));
                phone.setPhoneNumber(rs.getString("numero"));
                phone.setPhoneType(rs.getString("tipo"));

                phones.add(phone);
            }

            return phones;
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
                DELETE FROM telefone
                WHERE id = ?
                """;

        PreparedStatement pstm = null;
        try {
            pstm = Database.getConnection().prepareStatement(query);

            pstm.setInt(1, k);

            pstm.execute();
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            Database.closeStatement(pstm);
        }
    }

    public void deleteAllPhoneNumberByContact(int contactId) {
        String query = """
                DELETE FROM telefone
                WHERE contato_id = ?
                """;
        PreparedStatement pstm = null;

        try {
            pstm = Database.getConnection().prepareStatement(query);

            pstm.setInt(1, contactId);
            pstm.execute();
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            Database.closeStatement(pstm);
        }
    }

    private Phone instantiatedPhone(ResultSet rs) throws SQLException {
        Phone ph = new Phone();
        ph.setId(rs.getInt("id"));
        ph.setContactId(rs.getInt("contato_id"));
        ph.setPhoneNumber(rs.getString("numero"));
        ph.setPhoneType(rs.getString("tipo"));

        return ph;
    }

}
