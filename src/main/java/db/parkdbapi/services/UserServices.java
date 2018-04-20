package db.parkdbapi.services;

import db.parkdbapi.models.ErrorModel;
import db.parkdbapi.models.UserModel;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class UserServices {
    JdbcTemplate jdbcTemplate;

    private RowMapper<UserModel> readUserMapper = (rs, i) ->
            new UserModel(rs.getString("nickname"),
                    rs.getString("fullname"),
                    rs.getString("email"),
                    rs.getString("about"));


    public UserServices(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public UserModel create(UserModel model, String nickname){
        String query = "insert into users (nickname, fullname, email, about) values (?, ?, ?, ?);";
        jdbcTemplate.update(query, nickname, model.getFullname(), model.getEmail(), model.getAbout());
        model.setNickname(nickname);
        return model;
    }

    public List<UserModel> getDuplicate(String email, String nickname) {
        String query = "select * from users where email = (?) or nickname = (?);";
        return jdbcTemplate.query(query, readUserMapper, email, nickname);
    }

    public UserModel getProfile(String nickname){
        String query = "select * from users where nickname = (?);";
        //return jdbcTemplate.queryForObject(query, UserModel, nickname);
        return jdbcTemplate.query(query, readUserMapper, nickname).get(0);
    }

    public UserModel updateProfile(UserModel model, String nickname){
        if(model.getAbout() == null && model.getEmail() == null &&
                model.getFullname() == null && model.getNickname() == null){
            String query = "select * from users where nickname = (?);";
            model = jdbcTemplate.query(query, readUserMapper, nickname).get(0);
        }
        else {
            /*
            String query = "UPDATE users SET ";
            if(model.getAbout() != null)
                query += "about = (?)";
            if(model.getEmail() != null)
                query += ", email = (?)";
            if(model.getFullname() != null)
                query += ", fullname = (?)";
            query += " WHERE nickname = (?);";
             */
            String query = "UPDATE users SET about = (?), email = (?), fullname = (?)" +
                    " WHERE nickname = (?);";
            Integer flag = jdbcTemplate.update(query, model.getAbout(), model.getEmail(), model.getFullname(), nickname);
            if(flag == 0) {
                throw new DataAccessException(""){};
            }
        }
        model.setNickname(nickname);
        return model;
    }

    public ErrorModel notFoundProfile(String nickname){
        String message = "Can't find user by nickname: " + nickname;
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
