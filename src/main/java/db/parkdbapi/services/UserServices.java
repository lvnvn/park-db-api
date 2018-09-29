package db.parkdbapi.services;

import db.parkdbapi.models.BookModel;
import db.parkdbapi.services.BookServices;
import db.parkdbapi.models.ErrorModel;
import db.parkdbapi.models.UserModel;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServices {
    JdbcTemplate jdbcTemplate;

    public static RowMapper<UserModel> readUserMapper = (rs, i) ->
            new UserModel(rs.getString("nickname"),
                    rs.getString("fullname"),
                    rs.getString("email"),
                    rs.getInt("age"),
                    rs.getInt("id"));


    public UserServices(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public UserModel create(UserModel model, String email){
        String query = "insert into users (nickname, fullname, email, age) values (?, ?, ?, ?);";
        jdbcTemplate.update(query, model.getNickname(), model.getFullname(), email, model.getAge());
        model.setEmail(email);

        query = "select id from users where email = (?);";
        model.setId(jdbcTemplate.queryForObject(query, Integer.class, model.getEmail()));
        return model;
    }

    public List<UserModel> getDuplicate(String email) {
        String query = "select * from users where email = (?);";
        return jdbcTemplate.query(query, readUserMapper, email);
    }

    public UserModel getProfile(String email){
        String query = "select * from users where email = (?);";
        return jdbcTemplate.query(query, readUserMapper, email).get(0);
    }


    public ErrorModel notFoundProfile(String email){
        String message = "Can't find user by email: " + email;
        ErrorModel model = new ErrorModel(message);
        return model;
    }

    public ErrorModel duplicateProfile(String email){
        String query = "select * from users where email = (?);";
        String nickname =  jdbcTemplate.query(query, readUserMapper, email).get(0).getNickname();
        String message = "This email is already registered by user: " + nickname;
        ErrorModel model = new ErrorModel(message);
        return model;
    }
}
