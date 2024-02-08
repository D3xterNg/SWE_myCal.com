package sg.edu.ntu.scse.dgjmqz.myCal.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import sg.edu.ntu.scse.dgjmqz.myCal.model.FlatPriceEstimateModel;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;

import sg.edu.ntu.scse.dgjmqz.myCal.entities.FlatPriceEstimate;

@RestController
@RequestMapping("/api/v1/flatPriceEstimate")
public class FlatPriceEstimateCtr {

    @Autowired
    FlatPriceEstimateModel flatPriceEstimateModel;

    @GetMapping
    public List<FlatPriceEstimate> getFlatPriceEstimate()
            throws ExecutionException, InterruptedException {
        return FlatPriceEstimateModel.getFlatPriceEstimate();
    }

    // @Validated(PostValidation.class) @RequestBody
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postFlatPriceEstimate(@RequestBody FlatPriceEstimate flatPriceEstimate) {

        try {
            String flatPriceEstimateId = FlatPriceEstimateModel.createFlatPriceEstimate(flatPriceEstimate);
            return new ResponseEntity<>(flatPriceEstimateId, HttpStatus.CREATED);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
