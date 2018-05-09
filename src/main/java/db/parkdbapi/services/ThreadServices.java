package db.parkdbapi.services;

import db.parkdbapi.models.ErrorModel;
import db.parkdbapi.models.ThreadModel;
import db.parkdbapi.models.VoteModel;
import db.parkdbapi.services.ForumServices;
import db.parkdbapi.services.UserServices;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.dao.UncategorizedDataAccessException;
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

    public static RowMapper<VoteModel> readVoteMapper = (rs, i) ->
            new VoteModel(rs.getString("nickname"),
                    rs.getInt("thread"),
                    rs.getInt("voice"));

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
        query = "select * from forums where slug = (?);";
        if(jdbcTemplate.query(query, ForumServices.readForumMapper, model.getForum()).size() == 0)
            throw new InvalidDataAccessResourceUsageException(""); //forum not found

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
        query = "select * from forums where slug = (?);";
        model.setForum(jdbcTemplate.query(query, ForumServices.readForumMapper, model.getForum()).get(0).getSlug());
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

    public ThreadModel vote(String slug_or_id, VoteModel model) {
        Integer id = -1;
        String query;
        Integer delta = 0;
        try{
            id = Integer.parseInt(slug_or_id);
        }catch(NumberFormatException e){}

        if(id == -1) {
            query = "select id from threads where slug = (?)";
            id = jdbcTemplate.queryForObject(query, Integer.class, slug_or_id);
        }

        query = "select * from votes where nickname = (?) and thread = (?)";
        if(jdbcTemplate.query(query, readVoteMapper, model.getNickname(), id).size() == 0){
            query = "insert into votes (nickname, thread, voice) values (?, ?, ?);";
            jdbcTemplate.update(query, model.getNickname(), id, model.getVoice());
        } else {
            query = "select voice from votes where thread = (?) and nickname = (?)";
            delta = jdbcTemplate.queryForObject(query, Integer.class, id, model.getNickname());
            query = "update votes set voice = (?) where thread = (?) and nickname = (?);";
            jdbcTemplate.update(query, model.getVoice(), id, model.getNickname());
        }

        query = "select votes from threads where id = (?)";
        Integer votes = jdbcTemplate.queryForObject(query, Integer.class, id);
        votes = votes + model.getVoice() - delta;
        query = "UPDATE threads SET votes = (?) WHERE id = (?);";
        jdbcTemplate.update(query, votes, id);

        query = "select * from threads where id = (?)";
        return jdbcTemplate.query(query, readThreadMapper, id).get(0);
    }

    public ThreadModel get(String slug_or_id) {
        Integer id = -1;
        String query;
        try {
            id = Integer.parseInt(slug_or_id);
        } catch (NumberFormatException e) {
        }

        if (id == -1) {
            query = "select id from threads where slug = (?)";
            id = jdbcTemplate.queryForObject(query, Integer.class, slug_or_id);
        }
        query = "select * from threads where id = (?)";
        return jdbcTemplate.query(query, readThreadMapper, id).get(0);
    }
}
