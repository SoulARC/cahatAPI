package chatApi.controller;

import java.util.List;

import chatApi.model.User;
import chatApi.storage.Storage;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("http://localhost:63342")
public class UserController {

    @GetMapping("/active")
    public List<User> list(){
        return Storage.activeUserList;
    }
}
