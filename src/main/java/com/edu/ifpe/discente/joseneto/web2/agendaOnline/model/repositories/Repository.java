package com.edu.ifpe.discente.joseneto.web2.agendaOnline.model.repositories;

import java.sql.SQLException;
import java.util.List;

public interface Repository<ENTITY, KEY> {
    public void insert(ENTITY e) throws SQLException;

    public void update(ENTITY e) throws SQLException;

    public ENTITY findById(KEY k) throws SQLException;

    public List<ENTITY> findAll() throws SQLException;

    public void delete(KEY k) throws SQLException;
}
