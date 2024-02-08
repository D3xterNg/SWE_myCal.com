package sg.edu.ntu.scse.dgjmqz.myCal.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldMask;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

import sg.edu.ntu.scse.dgjmqz.myCal.entities.Property;

@Service
public class PropertyModel {
    public static List<Property> getProperties(@Nullable String title, @Nullable String price, int storey,
            @Nullable String flatType, @Nullable Date commenceDate, @Nullable String[] fields, int limit, int offset)
            throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();

        Query documentReference = dbFirestore.collection("properties");
        /* ArrayList<Filter> filters = new ArrayList<>(); */
        if (title != null) {
            documentReference = documentReference.whereEqualTo("title", title);
            /* filters.add(Filter.equalTo("title", title)); */
        }
        // logic for price range
        if (price != null) {
            int tempInt1 = 0;
            int tempInt2 = 0;

            if (price.charAt(0) == '<') {
                price = price.replaceAll("[^0-9]", "");
                tempInt1 = Integer.parseInt(price);
                documentReference = documentReference.whereLessThan("price", tempInt1);
                /* filters.add(Filter.lessThan("price", tempInt1)); */
            } else if (price.charAt(0) == '>') {
                price = price.replaceAll("[^0-9]", "");
                tempInt1 = Integer.parseInt(price);

                documentReference = documentReference.whereGreaterThan("price", tempInt1);/*
                                                                                           * 
                                                                                           * /* filters.add(Filter.
                                                                                           * greaterThan("price",
                                                                                           * tempInt1));
                                                                                           */
            } else {
                Pattern pattern = Pattern.compile("\\$([0-9]+)");
                Matcher matcher = pattern.matcher(price);
                String price1 = null;
                String price2 = null;

                if (matcher.find()) {
                    price1 = matcher.group(1);
                    tempInt1 = Integer.parseInt(price1);
                }

                if (matcher.find()) {
                    price2 = matcher.group(1);
                    tempInt2 = Integer.parseInt(price2);
                }

                documentReference = documentReference.whereGreaterThanOrEqualTo("price",
                        tempInt1)
                        .whereLessThanOrEqualTo("price", tempInt2);

                /*
                 * filters.add(Filter.greaterThanOrEqualTo("price", tempInt1));
                 * filters.add(Filter.lessThanOrEqualTo("price", tempInt2));
                 */

            }
        }

        /*
         * if (price != null) {
         * documentReference = documentReference.whereEqualTo("price", price);
         * }
         */
        if (storey != 0) {

            documentReference = documentReference.whereEqualTo("storey", storey);
            /* filters.add(Filter.equalTo("storey", storey)); */
        }
        if (flatType != null && !flatType.equals("all")) {
            documentReference = documentReference.whereEqualTo("flatType", flatType);
            /* filters.add(Filter.equalTo("flatType", flatType)); */
        }
        if (fields != null && fields.length > 0) {
            documentReference = documentReference.select(fields);
        }
        List<Property> properties = new ArrayList<>();
        if (commenceDate != null) {
            if (price != null) {
                Query documentReference_2 = dbFirestore.collection("properties");
                documentReference_2 = documentReference_2.whereGreaterThan("commenceDate",
                        commenceDate);

                if (title != null) {
                    documentReference_2 = documentReference_2.whereEqualTo("title", title);
                }
                if (storey != 0) {

                    documentReference_2 = documentReference_2.whereEqualTo("storey", storey);
                    /* filters.add(Filter.equalTo("storey", storey)); */
                }
                if (flatType != null && !flatType.equals("all")) {
                    documentReference_2 = documentReference_2.whereEqualTo("flatType", flatType);
                    /* filters.add(Filter.equalTo("flatType", flatType)); */
                }
                ApiFuture<QuerySnapshot> future1 = documentReference.get();
                ApiFuture<QuerySnapshot> future2 = documentReference_2.get();

                QuerySnapshot snapshot1 = future1.get();
                QuerySnapshot snapshot2 = future2.get();

                // Extract documents from the QuerySnapshots
                List<QueryDocumentSnapshot> documents1 = snapshot1.getDocuments();
                List<QueryDocumentSnapshot> documents2 = snapshot2.getDocuments();
                List<QueryDocumentSnapshot> matchingDocuments = new ArrayList<>();

                for (QueryDocumentSnapshot doc1 : documents1) {
                    for (QueryDocumentSnapshot doc2 : documents2) {
                        String id1 = doc1.getId();
                        String id2 = doc2.getId();

                        // Check for null values before performing the comparison
                        if (id1 != null && id2 != null && id1.equals(id2)) {
                            matchingDocuments.add(doc1);
                            break;
                        }
                    }
                }

                for (QueryDocumentSnapshot matchingDocument : matchingDocuments) {
                    Property property = matchingDocument.toObject(Property.class);
                    properties.add(property);
                }
                /*
                 * ApiFuture<QuerySnapshot> future_2 = documentReference.get();
                 * List<Property> properties_2 = future_2.get().toObjects(Property.class);
                 * List<Property> final_properties;
                 */

                /* filters.add(Filter.equalTo("title", title)); */
                return properties;
            } else {
                documentReference = documentReference.whereGreaterThan("commenceDate",
                        commenceDate);

            }
        }

        /* filters.add(Filter.greaterThan("commenceDate", commenceDate)); */

        /*
         * Filter[] temp = new Filter[filters.size()];
         * documentReference =
         * documentReference.where(Filter.and(filters.toArray(temp)));
         */
        ApiFuture<QuerySnapshot> future = documentReference.get();
        properties = future.get().toObjects(Property.class);
        properties.sort(Comparator.comparingLong(Property::getTime).reversed());
        return properties;
    }

    public static String getPropertySeller(String id) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection("properties").document(id);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            return document.getString("sellerId");
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    /*
     * public static List<Property> getPropertyByRecentlyAdded() throws
     * InterruptedException, ExecutionException {
     * 
     * Firestore dbFirestore = FirestoreClient.getFirestore();
     * Query query = dbFirestore.collection("properties");
     * return query.get().get().toObjects(Property.class);
     * 
     * }
     */

    public static Property getProperty(String id, @Nullable String[] fields)
            throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection("properties").document(id);
        ApiFuture<DocumentSnapshot> future = fields == null ? documentReference.get()
                : documentReference.get(FieldMask.of(fields));
        DocumentSnapshot document = future.get();

        Property property = null;

        if (document.exists()) {
            property = document.toObject(Property.class);
            return property;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

    public static String savePropertyDetails(Property property) throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference propRef = dbFirestore.collection("properties");
        ApiFuture<DocumentReference> addedDocRef = propRef.add(property);
        return addedDocRef.get().getId();
    }

    public static void updatePropertyDetails(String id, Property property)
            throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection("properties").document(id);
        String temString;
        int tempInt;
        HashMap<String, Object> map = new HashMap<>();
        Date commenceDate = property.getCommenceDate();
        if (commenceDate != null)
            map.put("commenceDate", commenceDate);
        temString = property.getTitle();
        if (temString != null && !temString.isEmpty())
            map.put("title", temString);
        temString = property.getDescription();
        if (temString != null && !temString.isEmpty())
            map.put("description", temString);
        temString = property.getFlatType();
        if (temString != null && !temString.isEmpty())
            map.put("flatType", temString);
        temString = property.getLocation();
        if (temString != null && !temString.isEmpty())
            map.put("location", temString);
        tempInt = property.getPrice();
        if (tempInt != 0)
            map.put("price", tempInt);
        tempInt = property.getSize();
        if (tempInt != 0)
            map.put("price", tempInt);
        temString = property.getPhotoExtension();
        if (temString != null && !temString.isEmpty())
            map.put("photoExtension", temString);
        tempInt = property.getStorey();
        if (tempInt > 0)
            map.put("storey", tempInt);
        tempInt = property.getNumOfBedrooms();
        if (tempInt > 0)
            map.put("numOfBedrooms", tempInt);
        tempInt = property.getNumOfBathrooms();
        if (tempInt > 0)
            map.put("numOfBathrooms", tempInt);
        // if (!documentReference.get().get().exists()) throw new
        // ResponseStatusException(HttpStatus.NOT_FOUND);
        documentReference.update(map);
    }

    public static void deleteProperty(String id) throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> writeResult = dbFirestore.collection("properties").document(id).delete();
    }

    public static List<Property> getPropertyByAccount(String accountId)
            throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        Query query = dbFirestore.collection("properties").whereEqualTo("sellerId", accountId);
        return query.get().get().toObjects(Property.class);
    }

    public static boolean isPropertyExists(String id) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        return dbFirestore.collection("properties").document(id).get().get().exists();
    }

    public static List<Property> getPropertiesByIds(List<String> ids) throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();

        List<Property> properties = new ArrayList<>();

        for (String id : ids) {
            DocumentReference documentReference = dbFirestore.collection("properties").document(id);
            ApiFuture<DocumentSnapshot> future = documentReference.get();
            DocumentSnapshot document = future.get();

            if (document.exists()) {
                Property property = document.toObject(Property.class);
                properties.add(property);
            } else {
                // If a property is not found, you might want to handle it according to your
                // requirements
                // For example, you can skip it or throw an exception
                // For demonstration, let's log a message
                System.out.println("Property with ID " + id + " not found.");
            }
        }

        return properties;
    }

    /*
     * public static List<Property> getPropertiesByRecent() throws
     * InterruptedException, ExecutionException {
     * Firestore dbFirestore = FirestoreClient.getFirestore();
     * 
     * List<Property> properties = new ArrayList<>();
     * 
     * Query query = dbFirestore.collection("properties");
     * ApiFuture<QuerySnapshot> querySnapshot = query.get();
     * 
     * for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
     * if (document.exists()) {
     * Property property = document.toObject(Property.class);
     * properties.add(property);
     * } else {
     * Property property = document.toObject(Property.class);
     * properties.add(property);
     * }
     * }
     * 
     * properties.sort(Comparator.comparingLong(Property::getTime).reversed());
     * 
     * return properties;
     * }
     */
}
