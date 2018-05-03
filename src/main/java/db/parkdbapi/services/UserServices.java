package db.parkdbapi.services;

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

    public UserModel updateProfile(UserModel model, String nickname) {
        /*System.out.println("wwwwwwwwwwwwww");
        if(model.getAbout() == null && model.getEmail() == null &&
                model.getFullname() == null && model.getNickname() == null){
            String query = "select * from users where nickname = (?);";
            model = jdbcTemplate.query(query, readUserMapper, nickname).get(0);
        }
        else {
            Integer flag = 0;
            String query = "select * from users where nickname = (?);";
            UserModel res_model = new UserModel(nickname,
                    jdbcTemplate.query(query, readUserMapper, nickname).get(0).getFullname(),
                    jdbcTemplate.query(query, readUserMapper, nickname).get(0).getEmail(),
                    jdbcTemplate.query(query, readUserMapper, nickname).get(0).getAbout());
            System.out.println("READ: " + res_model.getFullname() + res_model.getAbout());
            System.out.println("wwwwwwwwwwwwwwttttttttttfffffff");
            if(model.getAbout() != null) {
                query = "UPDATE users SET about = (?) WHERE nickname = (?);";
                flag = jdbcTemplate.update(query, model.getAbout(), nickname);
                res_model.setAbout(model.getAbout());
            }

            if(model.getEmail() != null) {
                query = "UPDATE users SET email = (?) WHERE nickname = (?);";
                flag = jdbcTemplate.update(query, model.getEmail(), nickname);
                res_model.setEmail(model.getEmail());
            }

            if(model.getFullname() != null) {
                query = "UPDATE users SET fullname = (?) WHERE nickname = (?);";
                flag = jdbcTemplate.update(query, model.getFullname(), nickname);
                res_model.setFullname(model.getFullname());
            }
            if(flag == 0) {
                throw new DataAccessException(""){};
            }
            res_model.setNickname(nickname);
            return res_model;
        }
        model.setNickname(nickname);
        return model;*/
        model.setNickname(nickname);

        String query = "update users set ";
        ArrayList<String> queryParams = new ArrayList<>();
        ArrayList<Object> params = new ArrayList<>();

        if (model.getEmail() != null) {
            queryParams.add(" email = ? ");
            params.add(model.getEmail());
        }
        if (model.getAbout() != null) {
            queryParams.add(" about = ? ");
            params.add(model.getAbout());
        }
        if (model.getFullname() != null) {
            queryParams.add(" fullname = ? ");
            params.add(model.getFullname());
        }

        if (queryParams.size() > 0) {
            query += queryParams.get(0);
            if (queryParams.size() > 1) {
                for (int i = 1; i < queryParams.size(); i++) {
                    query += ", ";
                    query += queryParams.get(i);
                }
            }
        }
        else {
            return model;
        }
        query += "where nickname = ?";
        params.add(model.getNickname());

        //final String sqlQuery = "update users set email = ?, about = ?, fullname = ? where nickname = ?";
        jdbcTemplate.update(query, params.toArray());
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
