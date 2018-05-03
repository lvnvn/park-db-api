package db.parkdbapi.services;

import db.parkdbapi.models.PostModel;
import db.parkdbapi.models.ThreadModel;
import db.parkdbapi.services.ThreadServices;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

@Service
public class PostService {
    JdbcTemplate jdbcTemplate;

    private RowMapper<PostModel> readPostMapper = (rs, i) -> {
        Timestamp ts = rs.getTimestamp("created");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        String created = df.format(ts.getTime());

        return new PostModel(rs.getString("author"),
                created,
                rs.getString("forum"),
                rs.getInt("id"),
                rs.getBoolean("edited"),
                rs.getString("message"),
                rs.getInt("parent"),
                rs.getInt("thread"));
    };

    public PostService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<PostModel> create(String slug_or_id, List<PostModel> models){
        Integer id = -1;
        try{
            id = Integer.parseInt(slug_or_id);
        }catch(NumberFormatException e){}
        ArrayList<PostModel> result = new ArrayList<>();
        for(Integer i = 0; i < models.size(); i++)
        {
            String query;
            result.add(models.get(i));

            /*query = "select * from users where nickname = (?)";
            if(jdbcTemplate.query(query, UserServices.readUserMapper, models.get(i).getAuthor()).size() == 0)
                throw new DataIntegrityViolationException("");*/

            if(id != -1) { // в запросе - id треда
                query = "select forum from threads where id = (?)";
                String forum = jdbcTemplate.queryForObject(query, String.class, id);
                models.get(i).setForum(forum);
                models.get(i).setThread(id);

                query = "insert into posts (author, thread, message) values (?, ?, ?);";
                jdbcTemplate.update(query, models.get(i).getAuthor(), id, models.get(i).getMessage());

            } else { // в запросе - slug треда
                query = "select * from threads where slug = (?)";
                ThreadModel thread = jdbcTemplate.query(query, ThreadServices.readThreadMapper, slug_or_id).get(0);
                models.get(i).setForum(thread.getForum());
                models.get(i).setThread(thread.getId());

                query = "insert into posts (author, thread, message) values (?, ?, ?);";
                jdbcTemplate.update(query, models.get(i).getAuthor(), thread.getId(), models.get(i).getMessage());
            }

            query = "select * from posts where message = (?)";
            PostModel mod = jdbcTemplate.query(query, readPostMapper, models.get(i).getMessage()).get(0);
            models.get(i).setId(mod.getId());
            models.get(i).setCreated(mod.getCreated());
        }
        return models;
    }
}
