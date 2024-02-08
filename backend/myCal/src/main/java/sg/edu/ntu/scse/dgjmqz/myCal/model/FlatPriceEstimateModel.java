package sg.edu.ntu.scse.dgjmqz.myCal.model;

import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.cloud.firestore.*;
import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.firebase.cloud.FirestoreClient;

import sg.edu.ntu.scse.dgjmqz.myCal.entities.FlatPriceEstimate;

@Service
public class FlatPriceEstimateModel {
    public static List<FlatPriceEstimate> getFlatPriceEstimate()
            throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        Query documentReference = dbFirestore.collection("flatPriceEstimate");

        ApiFuture<QuerySnapshot> future = documentReference.get();
        return future.get().toObjects(FlatPriceEstimate.class);
    }

    public static String createFlatPriceEstimate(FlatPriceEstimate flatPriceEstimate)
            throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference grantRef = dbFirestore.collection("flatPriceEstimate");
        ApiFuture<DocumentReference> addedDocRef = grantRef.add(flatPriceEstimate);
        return addedDocRef.get().getId();
    }
}
