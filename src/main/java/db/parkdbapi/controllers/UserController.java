package db.parkdbapi.controllers;

import db.parkdbapi.models.UserModel;
import db.parkdbapi.services.UserServices;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    UserServices uservice;

    public UserController(UserServices uservice) {
        this.uservice = uservice;
    }

    @RequestMapping(path="/api/user/{email}/create", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody UserModel model,@PathVariable String email){
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(uservice.create(model, email));
        }
        catch (DuplicateKeyException exc){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(uservice.getDuplicate(model.getEmail()));
        }
    }

    @RequestMapping(path="/api/user/{email}/profile", method = RequestMethod.GET)
    public ResponseEntity profile(@PathVariable String email){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(uservice.getProfile(email));
        }
        catch(IndexOutOfBoundsException exc){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(uservice.notFoundProfile(email));
        }
    }
}
