package db.parkdbapi.controllers;
import db.parkdbapi.services.ThreadServices;
import db.parkdbapi.models.ThreadModel;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class ThreadController {
    ThreadServices service;

    public ThreadController(ThreadServices service) {
        this.service = service;
    }

    @RequestMapping(path="/api/forum/{forum}/create", method = RequestMethod.POST)
    public ResponseEntity get(@PathVariable String forum, @RequestBody ThreadModel model) {
        //try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.create(model, forum));
        /*} catch (IndexOutOfBoundsException exc) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(service.notFoundForum(slug));
        }*/
    }

}
