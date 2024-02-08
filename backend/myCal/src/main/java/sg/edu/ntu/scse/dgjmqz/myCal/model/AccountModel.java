package sg.edu.ntu.scse.dgjmqz.myCal.model;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.checkerframework.checker.units.qual.A;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import sg.edu.ntu.scse.dgjmqz.myCal.entities.Account;
import sg.edu.ntu.scse.dgjmqz.myCal.entities.PersonalParticular.PersonalParticular;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AccountModel {
    /**
     * Create a new account
     * @param account
     * @return the account ID
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static String createAccount(Account account) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        CollectionReference propRef = firestore.collection("accounts");
        String email = account.getEmail().toLowerCase();
        if (!propRef.whereEqualTo("email", email).limit(1).get().get().isEmpty()) throw new ResponseStatusException(HttpStatus.CONFLICT);
        account.setEmail(email);
        PersonalParticular personalParticular = new PersonalParticular();
        String newID = createPersonalParticular(personalParticular);
        account.setPersonalParticularID(newID);
        ApiFuture<DocumentReference> addedDocRef = propRef.add(account);
        String accountID = addedDocRef.get().getId();
        System.out.print("\033[32mAdded account with ID: ");
        System.out.print(accountID);
        System.out.println("\033[0m");
        return accountID;
    }

    public static Account getAccount(String id) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentSnapshot docRef = firestore.collection("accounts").document(id).get(FieldMask.of("username", "email")).get();
        if (!docRef.exists()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        Account account = docRef.toObject(Account.class);
        return account;
    }
    public static Account getAccountByEmail(String email) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        QuerySnapshot docRef = firestore.collection("accounts").whereEqualTo("email", email.toLowerCase()).select("password").limit(1).get().get();
        if (docRef.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        QueryDocumentSnapshot snapshot = docRef.getDocuments().get(0);

        Account account = snapshot.toObject(Account.class);
        account.setAccountId(snapshot.getId());
        return account;
    }
    public static boolean isAccountExists(String id) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        return firestore.collection("accounts").document(id).get().get().exists();
    }
    public static void logout() throws ExecutionException, InterruptedException {
       // Account.setAccountId(null);
    }

    public static void addProperty(String userID, String propertyID) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference accountRef = firestore.collection("accounts").document(userID); // find the path
        ApiFuture<DocumentSnapshot> future_user = accountRef.get();
        DocumentSnapshot doc_property = future_user.get();
        List<String> arr = (List<String>) doc_property.get("propertyID");
        arr.add(propertyID);
        //.update("propertyID", arr);
    }

    public static void addSavedProperty(String userID, String propertyID) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        System.out.println(userID);
        DocumentReference userRef = firestore.collection("accounts").document(userID); // find the path
        if (!userRef.get().get().exists()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        //ApiFuture<DocumentSnapshot> future_user = userRef.get();
        //DocumentSnapshot doc_property = future_user.get();
        /*ArrayList<String> arr = (ArrayList<String>) doc_property.get("savedpropertyID");

        propertyID = propertyID.substring(1, propertyID.length() - 1);
        for(String str : arr){
            if(str.equals(propertyID)) return ;
        }
        arr.add(propertyID);*/
        userRef.update("savedPropertiesID", FieldValue.arrayUnion(propertyID));
    }
    public static List<String> getSavedProperty(String accountId) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentSnapshot userRef = firestore.collection("accounts").document(accountId).get().get(); // find the path
        if (!userRef.exists()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return (List<String>) userRef.get("savedPropertyID");

    }
    public static void deleteSavedProperty(String accountId, String propertyId) throws ExecutionException, InterruptedException {
        //if(Account.getAccoutId() == null) return;
        Firestore firestore = FirestoreClient.getFirestore(); // define the firestore
        // get the user
        DocumentReference userRef = firestore.collection("accounts").document(accountId);
        if (!userRef.get().get().exists()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        userRef.update("savedPropertyID", FieldValue.arrayRemove(propertyId));
        // find the path
        //Map<String, Object> updates = new HashMap<>();
        //updates.put("savedPropertyID", FieldValue.arrayRemove(ID));
        //accountRef.update(updates);
        //System.out.println("Deleted savedProperty for user " + Account.getUsername() + " with ID" + ID);
        return;
    }

    /**
     * Get the personal particular ID of the user
     * @param accountId
     * @return the personal particular ID
     */
    public static String getPersonalParticular(String accountId) {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference userRef = firestore.collection("accounts").document(accountId); // find the path
        ApiFuture<DocumentSnapshot> future_user = userRef.get();
        try {
            DocumentSnapshot documentSnapshot = future_user.get();
            String personalParticularID = (String) documentSnapshot.get("personalParticularID");
            System.out.println(personalParticularID);
            return personalParticularID;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static String createPersonalParticular(PersonalParticular personalParticular) throws ExecutionException, InterruptedException {

        System.out.println(personalParticular.getEmploymentStatus());
        Firestore firestore = FirestoreClient.getFirestore();
        CollectionReference propRef = firestore.collection("personalParticulars");
        ApiFuture<DocumentReference> addedDocRef = propRef.add(personalParticular);
        String personalParticularID = addedDocRef.get().getId();
        System.out.print("\033[32mAdded personalParticular with ID: ");
        System.out.print(personalParticularID);
        System.out.println("\033[0m");
        return personalParticularID;
    }

    public static void deletePersonalParticular(String personalParticularID) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference userRef = firestore.collection("personalParticulars").document(personalParticularID); // find the path
        userRef.delete();
        System.out.println("Deleted personalParticular with ID" + personalParticularID);
    }

    public static void updatePersonalParticular(String accountId, PersonalParticular personalParticular) throws ExecutionException, InterruptedException {
        deletePersonalParticular(getPersonalParticular(accountId));
        String newID = createPersonalParticular(personalParticular);
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference userRef = firestore.collection("accounts").document(accountId); // find the path
        userRef.update("personalParticularID", newID);
    }

    public static PersonalParticular viewPersonalParticular(String accountId) {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference userRef = firestore.collection("accounts").document(accountId); // find the path
        ApiFuture<DocumentSnapshot> future_user = userRef.get();
        try {
            DocumentSnapshot doc_property = future_user.get();
            String personalParticularID = (String) doc_property.get("personalParticularID");
            DocumentReference personalParticularRef = firestore.collection("personalParticulars").document(personalParticularID);
            ApiFuture<DocumentSnapshot> future_personalParticular = personalParticularRef.get();
            DocumentSnapshot doc_personalParticular = future_personalParticular.get();
            return doc_personalParticular.toObject(PersonalParticular.class);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static void saveProperty(String accountId, String propertyId) {
        try{
            Firestore firestore = FirestoreClient.getFirestore();
            DocumentReference userRef = firestore.collection("accounts").document(accountId); // find the path
            DocumentSnapshot doc = userRef.get(FieldMask.of("savedPropertyID")).get();
            ArrayList<String> savedPropertyID =  (ArrayList<String>)doc.get("savedPropertyID");
            if (savedPropertyID != null && savedPropertyID.contains(propertyId)) {
                System.out.println("Property already saved for this user.");
                throw new ResponseStatusException(HttpStatus.CONFLICT);

            } else {
                userRef.update("savedPropertyID", FieldValue.arrayUnion(propertyId));
                System.out.println("Property saved successfully.");
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static boolean hasSaveProperty(String accountId, String propertyId) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference userRef = firestore.collection("accounts").document(accountId); // find the path
        DocumentSnapshot doc = userRef.get(FieldMask.of("savedPropertyID")).get();
        ArrayList<String> savedPropertyID = (ArrayList<String>)doc.get("savedPropertyID");
        return savedPropertyID != null && savedPropertyID.contains(propertyId);

    }

    public static void bookProperty(String accountId, String propertyId) {
        try{
            Firestore firestore = FirestoreClient.getFirestore();
            DocumentReference userRef = firestore.collection("accounts").document(accountId); // find the path
            DocumentSnapshot doc = userRef.get(FieldMask.of("bookedPropertyID")).get();
            ArrayList<String> bookedPropertyID =  (ArrayList<String>)doc.get("bookedPropertyID");
            if(bookedPropertyID != null && bookedPropertyID.contains(propertyId)) {
                System.out.println("Property already booked for this user.");
                throw new ResponseStatusException(HttpStatus.CONFLICT);
            } else {
                userRef.update("bookedPropertyID", FieldValue.arrayUnion(propertyId));
                System.out.println("Property booked successfully.");
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static List<String> getBookedProperty(String accountId) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentSnapshot userRef = firestore.collection("accounts").document(accountId).get().get(); // find the path
        if (!userRef.exists()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return (List<String>) userRef.get("bookedPropertyID");

    }

    public static void deleteBookedProperty(String accountId, String propertyId) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference userRef = firestore.collection("accounts").document(accountId); // find the path
        if(!userRef.get().get().exists()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        userRef.update("bookedPropertyID", FieldValue.arrayRemove(propertyId));
    }

    /* 
    public static List<String> getPurchasedPropID(String accountId) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentSnapshot userRef = firestore.collection("accounts").document(accountId).get().get(); // find the path
        if (!userRef.exists()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return (List<String>) userRef.get("purchasedPropID");
    }*/
}
