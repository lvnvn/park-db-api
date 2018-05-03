package db.parkdbapi.services;

import db.parkdbapi.models.ErrorModel;
import db.parkdbapi.models.ThreadModel;
import db.parkdbapi.services.UserServices;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

@Service
public class ThreadServices {
    JdbcTemplate jdbcTemplate;

    public static RowMapper<ThreadModel> readThreadMapper = (rs, i) -> {
        Timestamp ts = rs.getTimestamp("created");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        String created = df.format(ts.getTime());
        return new ThreadModel(rs.getString("author"),
                created,
                rs.getString("forum"),
                rs.getInt("id"),
                rs.getString("message"),
                rs.getString("slug"),
                rs.getString("title"),
                rs.getInt("votes"));
    };
    public ThreadServices(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ThreadModel create(ThreadModel model, String forum){
        String query;
        query = "select * from threads where slug = (?);";
        if(jdbcTemplate.query(query, readThreadMapper, model.getSlug()).size() == 1)
            throw new DataIntegrityViolationException(""); //conflict
        query = "select * from users where nickname = (?);";
        if(jdbcTemplate.query(query, UserServices.readUserMapper, model.getAuthor()).size() == 0)
            throw new DataSourceLookupFailureException(""); //user not found

        if(model.getCreated() == null) {
            query = "insert into threads (author, forum, message, title, slug) values (?, ?, ?, ?, ?);";
            jdbcTemplate.update(query, model.getAuthor(), forum, model.getMessage(), model.getTitle(), model.getSlug());
        } else {
            query = "insert into threads (author, created, forum, message, title, slug) values (?, ?, ?, ?, ?, ?);";
            jdbcTemplate.update(query, model.getAuthor(), model.getCreated(), forum,
                    model.getMessage(), model.getTitle(), model.getSlug());
        }
        query = "select * from threads where title = (?);";
        model.setId(jdbcTemplate.query(query, readThreadMapper, model.getTitle()).get(0).getId());
        return model;
    }

    public List<ThreadModel> getAll(String forum, Boolean flag, Integer n, String since){
        String query = "select * from forums where slug = (?);";
        if(jdbcTemplate.query(query, ForumServices.readForumMapper, forum).size() == 0)
            throw new DataIntegrityViolationException("");
        List<ThreadModel> models;
        query = "select * from threads where forum = (?)";
        if(since != null){
            if(flag != null && flag == true)
                    query += " and created <= (?) order by created desc";
            else query += " and created >= (?)  order by created asc";
        }

        else if(flag != null && flag == true)
            query += " order by created desc";
        else query += " order by created asc";

        if(n != null) {
            query += " limit (?);";
            if(since == null) models = jdbcTemplate.query(query, readThreadMapper, forum, n);
            else models = jdbcTemplate.query(query, readThreadMapper, forum, since, n);
        } else {
            query += ";";
            if(since == null) models = jdbcTemplate.query(query, readThreadMapper, forum);
            else models = jdbcTemplate.query(query, readThreadMapper, forum, since);
        }
        /*if(models.size() == 0){
            throw new IndexOutOfBoundsException();
        } else {*/
            return models;
        //}
    }

    public ErrorModel notFoundForum(String slug){
        String message = "Can't find forum with slug: " + slug;
        ErrorModel model = new ErrorModel(message);
        return model;
    }

    public ErrorModel notFoundUser(ThreadModel tmodel){
        String message = "Can't find thread author by nickname: " + tmodel.getAuthor();
        ErrorModel model = new ErrorModel(message);
        return model;
    }

    public ThreadModel getThread(ThreadModel model) {
        String query = "select * from threads where slug = (?);";
        return jdbcTemplate.query(query, readThreadMapper, model.getSlug()).get(0);
    }
}
