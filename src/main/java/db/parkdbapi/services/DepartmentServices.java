package db.parkdbapi.services;


import db.parkdbapi.models.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentServices {
    JdbcTemplate jdbcTemplate;

    public static RowMapper<DepartmentModel> readDepartmentMapper = (rs, i) ->
            new DepartmentModel(rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("books"));

    public DepartmentServices(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public DepartmentModel create(String name){
        String query = "insert into departments (name, books) values (?, ?);";
        jdbcTemplate.update(query, name, 0);
        return get(name);
    }

    public DepartmentModel get(String name){
        String query = "select * from departments where name = (?);";
        return jdbcTemplate.query(query, readDepartmentMapper, name).get(0);
    }

    public List<DepartmentModel> getAll(){
        String query = "select * from departments;";
        return jdbcTemplate.query(query, readDepartmentMapper);
    }

}