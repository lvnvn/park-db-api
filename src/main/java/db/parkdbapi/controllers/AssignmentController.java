package db.parkdbapi.controllers;


import db.parkdbapi.services.AssignmentServices;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AssignmentController {
    AssignmentServices service;

    public AssignmentController(AssignmentServices service) {
        this.service = service;
    }

    @RequestMapping(path = "/api/assign", method = RequestMethod.POST)
    public ResponseEntity assign(@RequestParam(value = "uid", required = true) Integer uid,
                                 @RequestParam(value = "bid", required = true) Integer bid) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.assign(uid, bid));
        } catch (EmptyResultDataAccessException exc) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(service.NotFound(uid, bid));
        } catch (DuplicateKeyException exc) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(service.AlreadyAssigned(bid));
        } catch (DataIntegrityViolationException exc) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(service.TooMuchAssigned(uid));
        }
    }

    @RequestMapping(path = "/api/return", method = RequestMethod.POST)
    public ResponseEntity returnAssigned(@RequestParam(value = "uid", required = true) Integer uid,
                                 @RequestParam(value = "bid", required = true) Integer bid) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.returnAssigned(uid, bid));
        } catch (DataIntegrityViolationException exc) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(service.AssignmentNotFound(uid, bid));
        }
    }
}