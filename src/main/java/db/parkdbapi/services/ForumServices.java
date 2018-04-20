package db.parkdbapi.services;

import db.parkdbapi.models.ForumModel;
import db.parkdbapi.models.UserModel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class ForumServices {
    JdbcTemplate jdbcTemplate;

    /*private RowMapper<ForumModel> readForumMapper = (rs, i) ->
            new ForumModel(rs.getInt("posts"),
                    rs.getString("slug"),
                    rs.getInt("threads"),
                    rs.getString("title"),
                    rs.getString("user_nickname"));*/

    public ForumServices(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ForumModel create(ForumModel model){
        String query = "insert into forums (slug, title, user_nickname) values (?, ?, ?);";
        jdbcTemplate.update(query, model.getSlug(), model.getTitle(), model.getUser());
        return model;
    }


}
