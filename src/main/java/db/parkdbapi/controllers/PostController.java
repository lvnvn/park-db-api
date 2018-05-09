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
            return ResponseEntity.status(HttpStatus.CREATED).body(service.create(slug_or_id, models));
    }
    @RequestMapping(path="/api/thread/{slug_or_id}/posts", method = RequestMethod.GET)
    public ResponseEntity getAll(@PathVariable String slug_or_id,
                                 @RequestParam(value = "limit", required = false) Integer n,
                                 @RequestParam(value = "sort", required = false) String sort,
                                 @RequestParam(value = "desc", required = false) Boolean order) {
        return ResponseEntity.status(HttpStatus.OK).body(service.get(slug_or_id, n, sort, order));
    }


}

