package sg.edu.ntu.scse.dgjmqz.myCal.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.cloud.firestore.annotation.DocumentId;

public class Grant {

    @DocumentId
    private String id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String income;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String familyNucleusStr;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String flatTypeStr;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String ownershipHistoryStr;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String discount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getFamilyNucleusStr() {
        return familyNucleusStr;
    }

    public void setFamilyNucleusStr(String familyNucleusStr) {
        this.familyNucleusStr = familyNucleusStr;
    }

    public String getFlatTypeStr() {
        return flatTypeStr;
    }

    public void setFlatTypeStr(String flatTypeStr) {
        this.flatTypeStr = flatTypeStr;
    }

    public String getOwnershipHistoryStr() {
        return ownershipHistoryStr;
    }

    public void setOwnershipHistoryStr(String ownershipHistoryStr) {
        this.ownershipHistoryStr = ownershipHistoryStr;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

}
