package db.parkdbapi.controllers;

import db.parkdbapi.models.ForumModel;
import db.parkdbapi.models.UserModel;
import db.parkdbapi.services.ForumServices;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ForumController {
    ForumServices service;

    public ForumController(ForumServices service) {
        this.service = service;
    }

    @RequestMapping(path="/api/forum/create", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody ForumModel model){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(model));
    }
}
