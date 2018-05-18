package db.parkdbapi.services;

import db.parkdbapi.models.ForumModel;
import db.parkdbapi.models.ErrorModel;
import db.parkdbapi.models.UserModel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class ForumServices {
    JdbcTemplate jdbcTemplate;
    UserServices userService;

    public static RowMapper<ForumModel> readForumMapper = (rs, i) ->
            new ForumModel(rs.getInt("posts"),
                    rs.getString("slug"),
                    rs.getInt("threads"),
                    rs.getString("title"),
                    rs.getString("user_nickname"));

    public ForumServices(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        userService = new UserServices(jdbcTemplate);
    }

    public ForumModel create(ForumModel model){
        String query = "insert into forums (slug, title, user_nickname) values (?, ?, ?);";
        jdbcTemplate.update(query, model.getSlug(), model.getTitle(), model.getUser());
        model.setUser(userService.getProfile(model.getUser()).getNickname());
        return model;
    }

    public ForumModel get(String slug){
        String query = "select * from forums where slug = (?);";
        ForumModel model =  jdbcTemplate.query(query, readForumMapper, slug).get(0);
        model.setUser(userService.getProfile(model.getUser()).getNickname());
        return model;
    }

    public ErrorModel notFoundUser(String nickname){
        String message = "Can't find user by nickname: " + nickname;
        ErrorModel model = new ErrorModel(message);
        return model;
    }

    public ErrorModel notFoundForum(String slug){
        String message = "Can't find forum with slug: " + slug;
        ErrorModel model = new ErrorModel(message);
        return model;
    }

}
