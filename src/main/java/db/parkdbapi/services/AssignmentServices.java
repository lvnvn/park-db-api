package db.parkdbapi.services;

import db.parkdbapi.models.AssignmentModel;
import db.parkdbapi.models.ErrorModel;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.Date;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

@Service
public class AssignmentServices {
    JdbcTemplate jdbcTemplate;

    public static RowMapper<AssignmentModel> readAssignmentMapper = (rs, i) -> {
        Timestamp ts = rs.getTimestamp("due");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        String due = df.format(ts.getTime());

        return new AssignmentModel(rs.getInt("bookid"),
                rs.getInt("userid"),
                due);

    };

    public AssignmentServices(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public AssignmentModel assign(Integer uid, Integer bid){
        //throws exception if user or book doesnt exist
        String query = "select nickname from users where id = (?);";
        jdbcTemplate.queryForObject(query, String.class, uid);
        query = "select title from books where id = (?);";
        jdbcTemplate.queryForObject(query, String.class, uid);

        // throws exception if the book is assigned
        query = "select count(*) from assignments where bookid = (?) and active = true";
        //query = "select active from assignments where bookid = (?);";
        if(jdbcTemplate.queryForObject(query, Integer.class, bid) > 0) {
            throw new DuplicateKeyException(""){};
        }

        // throws exception if user has 5 books already assigned
        query = "select count(*) from assignments where userid = (?);";
        if(jdbcTemplate.queryForObject(query, Integer.class, uid) >= 5) {
            throw new DataIntegrityViolationException(""){};
        }

        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        Date due = new Date(now.getTime() + TimeUnit.DAYS.toMillis( 14 ));
        java.sql.Timestamp due_timestamp = new java.sql.Timestamp(due.getTime());

        query = "insert into assignments (bookid, userid, due) values (?, ?, ?);";
        jdbcTemplate.update(query, bid, uid, due_timestamp);

        return get(bid);
    }

    public AssignmentModel get(Integer bid) {
        String query = "select * from assignments where bookid = (?) and active = true;";
        return jdbcTemplate.query(query, readAssignmentMapper, bid).get(0);
    }

    public ErrorModel returnAssigned(Integer uid, Integer bid) {
        String query = "select count(*) from assignments where userid = (?) and bookid = (?) and active = true;";
        if(jdbcTemplate.queryForObject(query, Integer.class, uid, bid) == 0) {
            throw new DataIntegrityViolationException(""){};
        }
        ///query = "delete from assignments where bookid = (?);";
        query = "update assignments set active = false where bookid = (?);";
        jdbcTemplate.update(query,bid);

        String message = "Assigned book by id: " + bid + " returned by user by id: " + uid;
        ErrorModel model = new ErrorModel(message);
        return model;
    }

    public ErrorModel NotFound(Integer uid, Integer bid) {
        String message = "Can't find user by id: " + uid + " or book by id: " + bid;
        ErrorModel model = new ErrorModel(message);
        return model;
    }

    public ErrorModel AlreadyAssigned(Integer bid) {
        String message = "Book by id: " + bid + " is already assigned";
        ErrorModel model = new ErrorModel(message);
        return model;
    }

    public ErrorModel AssignmentNotFound(Integer uid, Integer bid) {
        String message = "Book by id: " + bid + " was not assigned to user by id: " + uid;
        ErrorModel model = new ErrorModel(message);
        return model;
    }

    public ErrorModel TooMuchAssigned(Integer uid) {
        String message = "Impossible to assign more than 5 books to user by id: " + uid;
        ErrorModel model = new ErrorModel(message);
        return model;
    }
}