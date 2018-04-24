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

    @RequestMapping(path="/api/user/{nickname}/create", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody UserModel model,@PathVariable String nickname){
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(uservice.create(model, nickname));
        }
        catch (DuplicateKeyException exc){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(uservice.getDuplicate(model.getEmail(), nickname));
        }
    }

    @RequestMapping(path="/api/user/{nickname}/profile", method = RequestMethod.GET)
    public ResponseEntity profile(@PathVariable String nickname){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(uservice.getProfile(nickname));
        }
        catch(IndexOutOfBoundsException exc){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(uservice.notFoundProfile(nickname));
        }

    }

    @RequestMapping(path="/api/user/{nickname}/profile", method = RequestMethod.POST)
    public ResponseEntity profile(@RequestBody UserModel model, @PathVariable String nickname){
        try {
            uservice.updateProfile(model, nickname);
            return ResponseEntity.status(HttpStatus.OK).body(uservice.getProfile(nickname));
        }
        catch (DuplicateKeyException exc){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(uservice.duplicateProfile(model.getEmail()));
        }
        catch (IndexOutOfBoundsException exc){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(uservice.notFoundProfile(nickname));
        }
    }
}
