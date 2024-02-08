package sg.edu.ntu.scse.dgjmqz.myCal.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.cloud.firestore.annotation.DocumentId;

public class FlatPriceEstimate {
    @DocumentId
    private String id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String location;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String flatType;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String estimatedPrice;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFlatType() {
        return flatType;
    }

    public void setFlatType(String flatType) {
        this.flatType = flatType;
    }

    public String getEstimatedPrice() {
        return estimatedPrice;
    }

    public void setEstimatedPrice(String estimatedPrice) {
        this.estimatedPrice = estimatedPrice;
    }
}
