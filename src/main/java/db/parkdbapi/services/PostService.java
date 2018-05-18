package db.parkdbapi.services;

import db.parkdbapi.models.PostModel;
import db.parkdbapi.models.ThreadModel;
import db.parkdbapi.services.ThreadServices;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        } catch(NumberFormatException e){}
        ArrayList<PostModel> result = new ArrayList<>();
        String currentTime = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));

        for(Integer i = 0; i < models.size(); i++)
        {
            String query;
            result.add(models.get(i));

            if(id == -1) { // в запросе - slug треда
                query = "select id from threads where slug = (?)";
                id = jdbcTemplate.queryForObject(query, Integer.class, slug_or_id);
            }

            query = "select forum from threads where id = (?)";
            String forum = jdbcTemplate.queryForObject(query, String.class, id);
            models.get(i).setForum(forum);
            models.get(i).setThread(id);

            query = "insert into posts (author, thread, message, created, forum) values (?, ?, ?, ?, ?);";
            jdbcTemplate.update(query, models.get(i).getAuthor(), id, models.get(i).getMessage(),
                        currentTime, models.get(i).getForum());

            query = "select id from posts where message = (?)";
            Integer postId = jdbcTemplate.queryForObject(query, Integer.class, models.get(i).getMessage());
            models.get(i).setId(postId);
            models.get(i).setCreated(currentTime);
            Array array;

            if(models.get(i).getParent() != null) {
                query = "select path from posts where id = (?)";
                array = jdbcTemplate.queryForObject(query, Array.class, models.get(i).getParent());
                query = "update posts set parent = (?) where id = (?)";
                jdbcTemplate.update(query, models.get(i).getParent(), postId);

                query = "update posts set path = array_append(?,?) where id = (?)";
                jdbcTemplate.update(query, array, postId, postId);
            } else {
                query = "update posts set path = array_append(array[]::integer[],?) where id = (?)";
                jdbcTemplate.update(query, postId, postId);
            }
            /*query = "select path from posts where id = (?)";
            System.out.println(jdbcTemplate.queryForObject(query, Array.class, postId));*/
        }
        return models;
    }

    public List<PostModel> get(String slug_or_id, Integer n, String sort, Boolean order, Integer since){
        List<PostModel> result = new ArrayList<>();
        String query;
        Integer id = -1;
        try{
            id = Integer.parseInt(slug_or_id);
        } catch(NumberFormatException e){}

        if(id == -1) { // в запросе - slug треда
            query = "select id from threads where slug = (?)";
            id = jdbcTemplate.queryForObject(query, Integer.class, slug_or_id);
        }


        if(sort == null || sort.equals("flat")) {
            if (order != null && order == true) {
                if (since != null)
                    query = "select * from posts where thread = (?) and id < (?) order by id desc";
                else
                    query = "select * from posts where thread = (?) order by id desc";
            } else {
                if (since != null)
                    query = "select * from posts where thread = (?) and id > (?) order by id asc";
                else
                    query = "select * from posts where thread = (?) order by id asc";
            }
            if(n != null) {
                query += " limit (?);";
                if(since == null)
                    return jdbcTemplate.query(query, readPostMapper, id, n);
                return jdbcTemplate.query(query, readPostMapper, id, since, n);
            }
            if(since == null)
                return  jdbcTemplate.query(query, readPostMapper, id);
            return  jdbcTemplate.query(query, readPostMapper, id, since);
        }

        if(sort.equals("tree")) {
            Array array = null;
            if(order != null && order == true) {
                if (since != null) {
                    query = "select path from posts where id = (?)";
                    array = jdbcTemplate.queryForObject(query, Array.class, since);
                    query = "select * from posts where thread = (?) and path < (?) order by path desc";
                }
                else
                    query = "select * from posts where thread = (?) order by path desc";
            } else {
                if (since != null) {
                    query = "select path from posts where id = (?)";
                    array = jdbcTemplate.queryForObject(query, Array.class, since);
                    query = "select * from posts where thread = (?) and path > (?) order by path asc";
                }
                else
                    query = "select * from posts where thread = (?) order by path asc";
            }

            if(n != null) {
                query += " limit (?);";
                if(since == null)
                    return jdbcTemplate.query(query, readPostMapper, id, n);
                return jdbcTemplate.query(query, readPostMapper, id, array, n);
            }
            if(since == null)
                return  jdbcTemplate.query(query, readPostMapper, id);
            return  jdbcTemplate.query(query, readPostMapper, id, array);
        }

        if(sort.equals("parent_tree")) {
            if(n != null) {
                Array limitPath = null;
                if(order != null && order == true)
                    query = "select * from posts where thread = (?) and parent = 0 order by id desc limit (?)";
                else
                    query = "select * from posts where thread = (?) and parent = 0 order by id asc limit (?)";

                List<PostModel> p = jdbcTemplate.query(query, readPostMapper, id, n);
                if(p.size() > n-1) {
                    query = "select path from posts where id = (?)";
                    limitPath = jdbcTemplate.queryForObject(query, Array.class, p.get(n-1).getId());
                    if(order != null && order == true)
                        query = "select * from posts where thread = (?) and path >= (?) order by path desc";
                    else
                        query = "select * from posts where thread = (?) and path <= (?) order by path asc";
                    return jdbcTemplate.query(query, readPostMapper, id, limitPath);
                }
            }
            if(order != null && order == true) {
                query = "select * from posts where thread = (?) and parent = 0 order by id desc";
                List<PostModel> p = jdbcTemplate.query(query, readPostMapper, id);
                    for(int i = 0; i < p.size(); i++) {
                        result.add(p.get(i));
                        List<PostModel> children = null;
                        Integer curr = p.get(i).getId();
                        System.out.println("added "+p.get(i).getId()+", prev = "+curr);
                        if(i == 0) {
                            query = "select * from posts where thread = (?) and path > '{" + curr +
                                    "}' and parent <> 0 order by path asc";
                        } else {
                            Integer prev = p.get(i - 1).getId();
                            query = "select * from posts where thread = (?) and path < '{" + prev + "}' and path > '{"
                                    + curr + "}' and parent <> 0 order by path asc";
                        }
                        children = jdbcTemplate.query(query, readPostMapper, id);

                        for (int j = 0; j < children.size(); j++){
                            result.add(children.get(j));
                            System.out.println("added "+children.get(j).getId()+", parent = "+children.get(j).getParent());
                        }
                    }
                    return result;
            }
            else
                query = "select * from posts where thread = (?) order by path asc";
            return jdbcTemplate.query(query, readPostMapper, id);
        }

        return result;
    }
}
