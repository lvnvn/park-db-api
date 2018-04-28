package db.parkdbapi.services;

import db.parkdbapi.models.ThreadModel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.RowMapper;

@Service
public class ThreadServices {
    JdbcTemplate jdbcTemplate;

    private RowMapper<ThreadModel> readThreadMapper = (rs, i) ->
            new ThreadModel(rs.getString("author"),
                    rs.getString("created"),
                    rs.getString("forum"),
                    rs.getInt("id"),
                    rs.getString("message"),
                    rs.getString("slug"),
                    rs.getString("title"),
                    rs.getInt("votes"));

    public ThreadServices(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ThreadModel create(ThreadModel model, String forum){
        String query = "insert into threads (author, created, forum, message, title) values (?, ?, ?, ?, ?);";
        jdbcTemplate.update(query, model.getAuthor(), model.getCreated(), forum,
                model.getMessage(), model.getTitle());
        query = "select * from threads where title = (?);";
        model.setId(jdbcTemplate.query(query, readThreadMapper, model.getTitle()).get(0).getId());
        return model;
    }

}
