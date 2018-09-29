package db.parkdbapi.services;


import db.parkdbapi.models.CountModel;
import db.parkdbapi.models.ErrorModel;
import db.parkdbapi.services.DepartmentServices;
import db.parkdbapi.models.BookModel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
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

    public static RowMapper<CountModel> readCountMapper = (rs, i) ->
            new CountModel(rs.getString("author"),
                    rs.getString("title"),
                    rs.getInt("assignments_count"));

    public BookServices(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public BookModel create(BookModel model){

        String query = "update departments set books = books + 1 where name = (?);";
        jdbcTemplate.update(query, model.getDepartment());

        query = "insert into books (title, publisher, author, department) values (?, ?, ?, ?);";
        jdbcTemplate.update(query, model.getTitle(), model.getPublisher(), model.getAuthor(), model.getDepartment());
        query = "select max(id) from books;";
        model.setId(jdbcTemplate.queryForObject(query, Integer.class));

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
        String query = "select id from departments where name = (?);";
        jdbcTemplate.queryForObject(query, Integer.class, department);

        query = "select * from books where department = (?) order by author;";
        return jdbcTemplate.query(query, readBookMapper, department);
    }

    public List<CountModel> getTop(Integer number){
        String query = "select books.author as author, count(*) as assignments_count, books.title as title from books inner join assignments on books.id = assignments.bookid group by books.id order by assignments_count DESC limit (?);";
        return jdbcTemplate.query(query, readCountMapper, number);
    }

    public ErrorModel notFoundDepartment(String dept){
        String message = "Can't find department by name: " + dept;
        ErrorModel model = new ErrorModel(message);
        return model;
    }
    public ErrorModel notFoundUser(Integer id){
        String message = "Can't find user by id: " + id;
        ErrorModel model = new ErrorModel(message);
        return model;
    }

    public List<BookModel> getUserAssigned(Integer id){
        String query = "select * from books where id in (select bookid from assignments where userid = (?) and active = TRUE order by author);";
        return jdbcTemplate.query(query, readBookMapper, id);
    }
}
