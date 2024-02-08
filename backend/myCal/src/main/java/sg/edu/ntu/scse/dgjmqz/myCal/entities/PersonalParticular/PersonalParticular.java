package sg.edu.ntu.scse.dgjmqz.myCal.entities.PersonalParticular;

import com.google.cloud.firestore.annotation.DocumentId;

public class PersonalParticular {
    @DocumentId
    /**
     * the ID of the PersonalParticular
     */
    private String personalParticularId = "";
    /**
     * The residency status of the user
     */
    private String residencyStatus;
    /**
     * The age range of the user
     */
    private String ageRange;
    /**
     * The marital status of the user
     */
    private String maritalStatus;
    /**
     * The employment status of the user
     */
    private String employmentStatus;
    /**
     * The income of the user
     */
    private String income;
    /**
     * The ownership history of the user
     */
    private String ownershipHistory;
    /**
     * The family nucleus of the user
     */
    private String familyNucleus;

    /**
     * Default constructor
     */
    public PersonalParticular() {
        this.residencyStatus = "NA";
        this.ageRange = "NA";
        this.maritalStatus = "NA";
        this.employmentStatus = "NA";
        this.income = "NA";
        this.ownershipHistory = "NA";
        this.familyNucleus = "NA";
    }

    /**
     * Constructor
     * @param residencyStatus The residency status of the user
     * @param ageRange The age range of the user
     * @param maritalStatus The marital status of the user
     * @param employmentStatus The employment status of the user
     * @param income The income of the user
     * @param ownershipHistory The ownership history of the user
     * @param familyNucleus The family nucleus of the user
     */
    public PersonalParticular(String residencyStatus, String maritalStatus, String employmentStatus, String ageRange, String ownershipHistory, String income, String familyNucleus) {
        this.residencyStatus = residencyStatus;
        this.maritalStatus = maritalStatus;
        this.employmentStatus = employmentStatus;
        this.ageRange = ageRange;
        this.ownershipHistory = ownershipHistory;
        this.income = income;
        this.familyNucleus = familyNucleus;
    }

    /**
     * get income
     * @return income
     */
    public String getIncome() {
        return this.income;
    }

    /**
     * set income
     * @param income income
     */
    public void setIncome(String income) {
        this.income = income;
    }

    /**
     * get personal particular id
     * @return personal particular id
     */
    public String getPersonalParticularId() {
        return this.personalParticularId;
    }

    /**
     * set personal particular id
     * @param personalParticularId personal particular id
     */
    public void setPersonalParticularId(String personalParticularId) {
        this.personalParticularId = personalParticularId;
    }

    /**
     * get residency status
     * @return residency status
     */
     public String getResidencyStatus() {
        return this.residencyStatus;
    }

    /**
     * get age range
     * @return age range
     */
    public String getAgeRange() {
        return this.ageRange;
    }

    /**
     * get marital status
     * @return marital status
     */
    public String getMaritalStatus() {
        return this.maritalStatus;
    }

    /**
     * get employment status
     * @return employment status
     */
    public String getEmploymentStatus() {
        return this.employmentStatus;
    }

    /**
     * set residency status
     * @param residencyStatus residency status
     */
    public void setResidencyStatus(String residencyStatus) {
        this.residencyStatus = residencyStatus;
    }

    /**
     * set age range
     * @param ageRange age range
     */
    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
    }

    /**
     * set marital status
     * @param maritalStatus marital status
     */
    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    /**
     * set employment status
     * @param employmentStatus employment status
     */
    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    /**
     * set ownership history
     * @param ownershipHistory ownership history
     */
    public void setOwnershipHistory(String ownershipHistory) {
        this.ownershipHistory = ownershipHistory;
    }

    /**
     * get ownership history
     * @return ownership history
     */
    public String getOwnershipHistory() {
        return this.ownershipHistory;
    }

    /**
     * set family nucleus
     * @return family nucleus
     */
    public String getFamilyNucleus() {
        return this.familyNucleus;
    }

    /**
     * set family nucleus
     * @param familyNucleus family nucleus
     */
    public void setFamilyNucleus(String familyNucleus) {
        this.familyNucleus = familyNucleus;
    }
}

