package db.parkdbapi.controllers;

import db.parkdbapi.models.BookModel;
import db.parkdbapi.services.BookServices;
import db.parkdbapi.services.DepartmentServices;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DepartmentController {
    DepartmentServices service;

    public DepartmentController(DepartmentServices service) {
        this.service = service;
    }

    @RequestMapping(path = "/api/department/create", method = RequestMethod.POST)
    public ResponseEntity create(@RequestParam(value = "name", required = true) String name) {
        try {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(name));
        } catch (DuplicateKeyException exc) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(service.get(name));
        }
    }

    @RequestMapping(path = "/api/department/get", method = RequestMethod.GET)
    public ResponseEntity getAll() {
            return ResponseEntity.status(HttpStatus.OK).body(service.getAll());
    }
}
