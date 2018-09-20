package db.parkdbapi.services;


import db.parkdbapi.services.DepartmentServices;
import db.parkdbapi.models.BookModel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookServices {
    JdbcTemplate jdbcTemplate;

    public static RowMapper<BookModel> readBookMapper = (rs, i) ->
            new BookModel(rs.getString("author"),
                    rs.getString("publisher"),
                    rs.getString("title"),
                    rs.getString("department"),
                    rs.getInt("id"));

    public BookServices(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public BookModel create(BookModel model){
        String query = "update departments set books = books + 1 where name = (?);";
        jdbcTemplate.update(query, model.getDepartment());

        query = "insert into books (title, publisher, author, department) values (?, ?, ?, ?);";
        jdbcTemplate.update(query, model.getTitle(), model.getPublisher(), model.getAuthor(), model.getDepartment());
        return model;
    }

    public List<BookModel> get(String author, String title){
        String value = "", query="";
        if(title != null) {
            query = "select * from books where title = (?);";
            value = title;
        }
        if(author != null) {
            query = "select * from books where author = (?);";
            value = author;
        }
        return jdbcTemplate.query(query, readBookMapper, value);
    }

    public List<BookModel> getDept(String department){
        String query = "select * from books where department = (?);";
        return jdbcTemplate.query(query, readBookMapper, department);
    }

}
