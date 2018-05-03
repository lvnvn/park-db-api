package db.parkdbapi.controllers;
import db.parkdbapi.services.ThreadServices;

import db.parkdbapi.models.ThreadModel;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;


@RestController
public class ThreadController {
    ThreadServices service;

    public ThreadController(ThreadServices service) {
        this.service = service;
    }

    @RequestMapping(path="/api/forum/{forum}/create", method = RequestMethod.POST)
    public ResponseEntity create(@PathVariable String forum, @RequestBody ThreadModel model) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.create(model, forum));
        } catch (DuplicateKeyException exc) {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.getThread(model));
        } catch (DataIntegrityViolationException exc) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(service.getThread(model));
        } catch (DataSourceLookupFailureException exc) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(service.notFoundUser(model));
        }
    }

    @RequestMapping(path="/api/forum/{forum}/threads", method = RequestMethod.GET)
    public ResponseEntity getAll(@PathVariable("forum") String forum,
                                 @RequestParam(value = "limit", required = false) Integer n,
                                 @RequestParam(value = "desc", required = false) Boolean flag,
                                 @RequestParam(value = "since", required = false) String since) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.getAll(forum, flag, n, since));
        } catch (DataIntegrityViolationException exc) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(service.notFoundForum(forum));
        }
    }


}
