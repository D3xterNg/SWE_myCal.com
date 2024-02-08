package sg.edu.ntu.scse.dgjmqz.myCal.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import sg.edu.ntu.scse.dgjmqz.myCal.model.GrantsModel;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;

import sg.edu.ntu.scse.dgjmqz.myCal.entities.Grant;

@RestController
@RequestMapping("/api/v1/grants")
public class GrantsCtr {

    @Autowired
    GrantsModel grantsModel;

    @GetMapping
    public List<Grant> getGrants()
            throws ExecutionException, InterruptedException {
        return GrantsModel.getGrants();
    }

    // @Validated(PostValidation.class) @RequestBody
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postGrant(@RequestBody Grant grant) {

        try {
            String grantId = GrantsModel.createGrant(grant);
            return new ResponseEntity<>(grantId, HttpStatus.CREATED);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
