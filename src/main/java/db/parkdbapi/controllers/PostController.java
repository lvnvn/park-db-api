package db.parkdbapi.controllers;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import db.parkdbapi.services.PostService;
import db.parkdbapi.models.PostModel;

import java.util.List;

@RestController
public class PostController {
    PostService service;

    public PostController(PostService service) {
        this.service = service;
    }

    @RequestMapping(path="/api/thread/{slug_or_id}/create", method = RequestMethod.POST)
    public ResponseEntity create(@PathVariable String slug_or_id, @RequestBody List<PostModel> models) {
        //try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.create(slug_or_id, models));
        /*} catch (DataIntegrityViolationException){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(service.onConflict(slug_or_id, models));
        }*/

    }

}

