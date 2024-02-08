package sg.edu.ntu.scse.dgjmqz.myCal.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sg.edu.ntu.scse.dgjmqz.myCal.entities.Account;
import sg.edu.ntu.scse.dgjmqz.myCal.model.AccountModel;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1/sessions")
public class SessionCtr {

    Argon2PasswordEncoder passwordEncoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    @PostMapping
    public ResponseEntity<String> login(@RequestBody Account.LoginForm loginForm, HttpSession session) throws ExecutionException, InterruptedException {
        String email = loginForm.getEmail();
        String password  = loginForm.getPassword();
        Account account = AccountModel.getAccountByEmail(email);
        if(passwordEncoder.matches(password, account.getPassword())) {
            session.setAttribute("accountID", account.getAccountId());
            String returnId = account.getAccountId();
            return new ResponseEntity<>(returnId, HttpStatus.CREATED);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

    }

    @GetMapping("/account")
    public Account getAccount(HttpSession session) throws ExecutionException, InterruptedException {
        String accountId = (String) session.getAttribute("accountID");
        if (accountId == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        return AccountModel.getAccount(accountId);
    }

    @DeleteMapping
    public  void logout(HttpSession session) {
        session.invalidate();
    }

}
