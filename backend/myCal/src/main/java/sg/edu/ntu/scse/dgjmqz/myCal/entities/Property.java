package sg.edu.ntu.scse.dgjmqz.myCal.entities;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.google.cloud.firestore.annotation.DocumentId;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import sg.edu.ntu.scse.dgjmqz.myCal.ValidationGroup.PostValidation;
import sg.edu.ntu.scse.dgjmqz.myCal.ValidationGroup.PutValidation;

public class Property {

    @DocumentId
//    @Null(groups = { PostValidation.class, PutValidation.class })
    private String id;
    @NotBlank(groups = { PostValidation.class })
    private String title;
    @NotBlank(groups = { PostValidation.class })
    private String description;
    @NotBlank(groups = { PostValidation.class })
    private String location;
    @NotBlank(groups = { PostValidation.class })
    private String flatType;
//    @Null(groups = { PostValidation.class, PutValidation.class })
    private String sellerId;

    private long time;
//    @Null(groups = { PostValidation.class, PutValidation.class })
    private String photoExtension;

    @PositiveOrZero(groups = { PutValidation.class })
    @Positive(groups = { PostValidation.class })
    private int storey;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date commenceDate;

    @PositiveOrZero(groups = { PutValidation.class })
    @Positive(groups = { PostValidation.class })
    private int numOfBedrooms;
    @PositiveOrZero(groups = { PutValidation.class })
    @Positive(groups = { PostValidation.class })
    private int numOfBathrooms;
    @PositiveOrZero(groups = { PutValidation.class })
    @Positive(groups = { PostValidation.class })
    private int size;
    @PositiveOrZero(groups = { PutValidation.class })
    @Positive(groups = { PostValidation.class })
    private int price;
    /*
     * private BookingDetails booking;
     */

    public Property() {
        this.title = "";
        this.description = "";
        this.location = "";
        this.flatType = "";
        this.sellerId = "";
        this.photoExtension = "";
        this.storey = 0;
        this.commenceDate = new Date();
        this.numOfBedrooms = 0;
        this.numOfBathrooms = 0;
        this.size = 0;
        this.price = 0;
        this.time = 0;
    }

    public Property(String title, String description, String location, String flatType, String sellerId,
            String photoExtension, int storey, Date commenceDate, int numOfBedrooms, int numOfBathrooms, int size,
            int price, long time) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.flatType = flatType;
        this.sellerId = sellerId;
        this.photoExtension = photoExtension;
        this.storey = storey;
        this.commenceDate = commenceDate;
        this.numOfBedrooms = numOfBedrooms;
        this.numOfBathrooms = numOfBathrooms;
        this.size = size;
        this.price = price;
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getNumOfBedrooms() {
        return numOfBedrooms;
    }

    public void setNumOfBedrooms(int numOfBedrooms) {
        this.numOfBedrooms = numOfBedrooms;
    }

    public int getNumOfBathrooms() {
        return numOfBathrooms;
    }

    public void setNumOfBathrooms(int numOfBathrooms) {
        this.numOfBathrooms = numOfBathrooms;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getPhotoExtension() {
        return photoExtension;
    }

    public void setPhotoExtension(String photoExtension) {
        this.photoExtension = photoExtension;
    }

    public int getStorey() {
        return storey;
    }

    public void setStorey(int storey) {
        this.storey = storey;
    }

    public Date getCommenceDate() {
        return commenceDate;
    }

    public void setCommenceDate(Date commenceDate) {
        this.commenceDate = commenceDate;
    }

    /*
     * public BookingDetails getBooking() {
     * return booking;
     * }
     * 
     * public void setBooking(BookingDetails booking) {
     * this.booking = booking;
     * }
     */
}
