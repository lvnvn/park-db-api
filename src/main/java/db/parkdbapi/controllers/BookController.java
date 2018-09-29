package db.parkdbapi.controllers;

import db.parkdbapi.models.BookModel;
import db.parkdbapi.services.BookServices;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BookController {
    BookServices service;

    public BookController(BookServices service) {
        this.service = service;
    }

    @RequestMapping(path="/api/book/create", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody BookModel model) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.create(model));
        } catch (DataIntegrityViolationException exc) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(service.notFoundDepartment(model.getDepartment()));
        }
    }
    @RequestMapping(path="/api/book/get", method = RequestMethod.GET)
    public ResponseEntity get(@RequestParam(value = "title", required = false) String t,
                                 @RequestParam(value = "author", required = false) String a) {
        //try {
            return ResponseEntity.status(HttpStatus.OK).body(service.get(a, t));
        /*} /*catch (IndexOutOfBoundsException exc) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(service.notFoundForum(slug));
        }*/
    }

    @RequestMapping(path="/api/book/{department}/get", method = RequestMethod.GET)
    public ResponseEntity getDept(@PathVariable String department) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.getDept(department));
        } catch (EmptyResultDataAccessException exc) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(service.notFoundDepartment(department));
        }
    }

    @RequestMapping(path="/api/book/top/{number}/get", method = RequestMethod.GET)
    public ResponseEntity getDept(@PathVariable Integer number) {
            return ResponseEntity.status(HttpStatus.OK).body(service.getTop(number));
        }

    @RequestMapping(path="/api/user/{id}/get", method = RequestMethod.GET)
    public ResponseEntity getUserAssigned(@PathVariable Integer id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.getUserAssigned(id));
        } catch (EmptyResultDataAccessException exc) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(service.notFoundUser(id));
        }
    }

}
