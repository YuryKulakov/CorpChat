package main.controllers;

import main.model.Message;
import main.model.User;
import main.repositories.MessageRepository;
import main.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;

@RestController
public class ChatController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageRepository messageRepository;

    @GetMapping(path = "/api/auth")
    public HashMap<String, Boolean> auth() {
        HashMap<String, Boolean> response = new HashMap<>();
        String getSessionId = getSessionId();
        User user = userRepository.getBySessionId(getSessionId);
        response.put("result", user != null);
        return response;
    }

    @PostMapping(path = "/api/users")
    public HashMap<String, Boolean> addUser(HttpServletRequest request) {
        String name = request.getParameter("name");
        String sessionId = getSessionId();
        User user = new User();
        user.setName(name);
        user.setSessionId(sessionId);
        user.setRegTime(new Date());
        userRepository.save(user);
        HashMap<String, Boolean> response = new HashMap<>();
        response.put("result", true);
        return response;
    }

    private String getSessionId() {
        return RequestContextHolder.currentRequestAttributes().getSessionId();
    }

    public HashMap<String,Boolean> addMessage(HttpServletRequest httpServletRequest){
        String text = httpServletRequest.getParameter("text");
        String sessionId = getSessionId();
        User user = userRepository.getBySessionId(sessionId);
        Message message = new Message();
        message.setTime(new Date());
        message.setMessage(text);
        message.setUser(user);
        messageRepository.save(message);

        HashMap<String, Boolean> response = new HashMap<>();
        response.put("result", true);
        return response;
    }
}
