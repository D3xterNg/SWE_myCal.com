package sg.edu.ntu.scse.dgjmqz.myCal.model;

import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.cloud.firestore.*;
import org.springframework.stereotype.Service;
import sg.edu.ntu.scse.dgjmqz.myCal.entities.Grant;
import com.google.api.core.ApiFuture;
import com.google.firebase.cloud.FirestoreClient;

@Service
public class GrantsModel {
    public static List<Grant> getGrants()
            throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        Query documentReference = dbFirestore.collection("grants");

        ApiFuture<QuerySnapshot> future = documentReference.get();
        return future.get().toObjects(Grant.class);
    }

    public static String createGrant(Grant grant) throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference grantRef = dbFirestore.collection("grants");
        ApiFuture<DocumentReference> addedDocRef = grantRef.add(grant);
        return addedDocRef.get().getId();
    }
}
