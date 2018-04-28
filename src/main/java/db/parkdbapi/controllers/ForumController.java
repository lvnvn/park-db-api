package db.parkdbapi.controllers;

import db.parkdbapi.models.ForumModel;
import db.parkdbapi.models.UserModel;
import db.parkdbapi.services.ForumServices;
import org.springframework.dao.DataIntegrityViolationException;
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
    public ResponseEntity create(@RequestBody ForumModel model) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.create(model));
        } catch (DuplicateKeyException exc) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(service.get(model.getSlug()));
        } catch (DataIntegrityViolationException exc) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(service.notFoundUser(model.getUser()));
        }
    }
    @RequestMapping(path="/api/forum/{slug}/details", method = RequestMethod.GET)
    public ResponseEntity get(@PathVariable String slug) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.get(slug));
        } catch (IndexOutOfBoundsException exc) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(service.notFoundForum(slug));
        }
    }

}
