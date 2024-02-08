package sg.edu.ntu.scse.dgjmqz.myCal.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.cloud.firestore.annotation.DocumentId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Null;
import java.util.ArrayList;

import sg.edu.ntu.scse.dgjmqz.myCal.ValidationGroup.PostValidation;
import sg.edu.ntu.scse.dgjmqz.myCal.ValidationGroup.PutValidation;

public class Account {
    /**
     * the ID of the Account
     */

    @DocumentId
    // @Null(message = "accountId not allowed",groups = { PostValidation.class, PutValidation.class })
    private String accountId = "";
    /**
     * email of the Account
     */
    @Email(groups = { PostValidation.class, PutValidation.class })
    @NotBlank(message = "email is mandatory", groups = { PostValidation.class })
    private String email;
    /**
     * the password of the Account
     */
    @NotBlank(message = "Password is mandatory", groups = { PostValidation.class })
    private String password;
    /**
     * username of the Account
     */
    @NotBlank(message = "username is mandatory", groups = { PostValidation.class })
    private String username;
    /**
     * a list of saved property ID
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private ArrayList<String> savedPropertyID = new ArrayList<String>();
    /**
     * a list of saved project ID
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private ArrayList<String> propertyID = new ArrayList<String>();
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private ArrayList<String> bookedPropertyID = new ArrayList<String>();
    private String personalParticularID;

    public String getPersonalParticularID() {
        return personalParticularID;
    }

    public void setPersonalParticularID(String personalParticularID) {
        this.personalParticularID = personalParticularID;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    /**
     * set username
     * 
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * get username
     * 
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * get email
     * 
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * set email
     * 
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * get password
     * 
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * set password
     * 
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public static class LoginForm {
        String email;
        String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

    }

    /**
     * set property ID
     * 
     * @param propertyID property ID
     */
    public void setPropertyID(ArrayList<String> propertyID) {
        this.propertyID = propertyID;
    }

    /**
     * get property ID
     * 
     * @return property ID
     */
    public ArrayList<String> getPropertyID() {
        return propertyID;
    }

    /**
     * set saved property ID
     * 
     * @param savedPropertyID saved property ID
     */
    public void setSavedPropertyID(ArrayList<String> savedPropertyID) {
        this.savedPropertyID = savedPropertyID;
    }

    /**
     * get saved property ID
     * 
     * @return saved property ID
     */
    public ArrayList<String> getSavedPropertyID() {
        return savedPropertyID;
    }

    public void setBookedPropertyID(ArrayList<String> bookedPropertyID) {
        this.bookedPropertyID = bookedPropertyID;
    }

    public ArrayList<String> getBookedPropertyID() {
        return bookedPropertyID;
    }
}
