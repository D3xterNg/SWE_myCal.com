package sg.edu.ntu.scse.dgjmqz.myCal.controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

import sg.edu.ntu.scse.dgjmqz.myCal.entities.Account;
import sg.edu.ntu.scse.dgjmqz.myCal.entities.BookingDetails;
import sg.edu.ntu.scse.dgjmqz.myCal.entities.Property;
import sg.edu.ntu.scse.dgjmqz.myCal.model.AccountModel;
import sg.edu.ntu.scse.dgjmqz.myCal.model.PropertyModel;
import sg.edu.ntu.scse.dgjmqz.myCal.ValidationGroup.PostValidation;
import sg.edu.ntu.scse.dgjmqz.myCal.service.EmailService;
import sg.edu.ntu.scse.dgjmqz.myCal.entities.PersonalParticular.PersonalParticular;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountCtr {
    Argon2PasswordEncoder passwordEncoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    @Autowired
    private EmailService emailService;

    @PostMapping()
    public ResponseEntity<String> createAccount(@Validated(PostValidation.class) @RequestBody Account account) {

        System.err.println(account.getEmail());
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        try {
            String accountId = AccountModel.createAccount(account);
            return new ResponseEntity<>(accountId, HttpStatus.CREATED);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("email")
    public void sendEmail(@RequestParam String email, String text) throws MessagingException {
        emailService.snedEmail(email, text);
    }

    @GetMapping("/{accountId}")
    public Account getAccount(@PathVariable String accountId) throws ExecutionException, InterruptedException {
        return AccountModel.getAccount(accountId);
    }

    @GetMapping("/{accountId}/property")
    public List<Property> getPropertyByAccount(@PathVariable String accountId)
            throws ExecutionException, InterruptedException {
        if (!AccountModel.isAccountExists(accountId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return PropertyModel.getPropertyByAccount(accountId);
    }

    @GetMapping("/{accountId}/savedProperty")
    public List<String> getSavedProperty(@PathVariable String accountId) throws ExecutionException, InterruptedException {
        if (!AccountModel.isAccountExists(accountId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return AccountModel.getSavedProperty(accountId);
    }

    @GetMapping("/{accountId}/saveProperty/{propertyId}")
    public ResponseEntity<Object> hasSaveProperty(@PathVariable String accountId, @PathVariable String propertyId) throws ExecutionException, InterruptedException {
        if (AccountModel.isAccountExists(accountId) && AccountModel.hasSaveProperty(accountId, propertyId)) {
            return ResponseEntity.noContent().build();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{accountId}/saveProperty/{propertyId}")
    public ResponseEntity<Object> saveProperty(@PathVariable String accountId, @PathVariable String propertyId, HttpSession session) {
        String userid = (String) session.getAttribute("accountID");
        if (userid == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        if (!userid.equals(accountId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        AccountModel.saveProperty(accountId, propertyId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{accountId}/saveProperty/{propertyId}")
    public ResponseEntity<Object> deleteSaveProperty(@PathVariable String accountId, @PathVariable String propertyId, HttpSession session) throws ExecutionException, InterruptedException {
        String userid = (String) session.getAttribute("accountID");
        if (userid == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        if (!userid.equals(accountId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        AccountModel.deleteSavedProperty(accountId, propertyId);
        return ResponseEntity.noContent().build();


    }

    @PostMapping("/{accountId}/updatePersonalParticular")
    public String updatePersonalParticular(@PathVariable String accountId, @RequestBody PersonalParticular personalParticular) throws ExecutionException, InterruptedException {
        if (!AccountModel.isAccountExists(accountId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        System.out.println(personalParticular.getResidencyStatus());
        AccountModel.updatePersonalParticular(accountId, personalParticular);
        return personalParticular.getPersonalParticularId();
    }

    @GetMapping("/{accountId}/viewPersonalParticular")
    public PersonalParticular viewPersonalParticular(@PathVariable String accountId) throws ExecutionException, InterruptedException {
        if (!AccountModel.isAccountExists(accountId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return AccountModel.viewPersonalParticular(accountId);
    }

    @PostMapping("/{accountId}/bookProperty/{propertyId}")
    public ResponseEntity<Object> bookProperty(@PathVariable String accountId, @PathVariable String propertyId, @RequestBody BookingDetails bookingDetails, HttpSession session) throws ExecutionException, InterruptedException, MessagingException {
        String userid = (String) session.getAttribute("accountID");
        if (userid == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        if (!userid.equals(accountId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        Property property = PropertyModel.getProperty(propertyId,new String[] { "sellerId", "title" });
        AccountModel.bookProperty(accountId, propertyId);
        Account seller = AccountModel.getAccount(property.getSellerId());
        Account buyer =  AccountModel.getAccount(accountId);
        emailService.snedEmail(seller.getEmail(), "hi " + seller.getUsername() + ", we would like to info you that " + buyer.getUsername() + " are interested on your property name "+ property.getTitle() + " and would like schedule a meeting with you on the " + bookingDetails.getBookingDate() +" you may contact him/she by " + bookingDetails.getBuyerContactNumber()+ " or " +  buyer.getEmail());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{accountId}/bookedProperty")
    public List<String> getBookedProperty(@PathVariable String accountId) throws ExecutionException, InterruptedException {
        if (!AccountModel.isAccountExists(accountId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return AccountModel.getBookedProperty(accountId);
    }
/* 
    @GetMapping("/{accountId}/purchasedPropID")
    public List<String> getPurchasedProperty(@PathVariable String accountId) throws ExecutionException, InterruptedException {
        if (!AccountModel.isAccountExists(accountId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return AccountModel.getPurchasedPropID(accountId);
    }
*/
    @DeleteMapping("/{accountId}/bookedProperty/{propertyId}")
    public ResponseEntity<Object> deleteBookedProperty(@PathVariable String accountId, @PathVariable String propertyId, HttpSession session) throws ExecutionException, InterruptedException {
        String userid = (String) session.getAttribute("accountID");
        if (userid == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        if (!userid.equals(accountId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        AccountModel.deleteBookedProperty(accountId, propertyId);
        return ResponseEntity.noContent().build();
    }
}
