package main.controllers;

import main.model.Message;
import main.model.User;
import main.repositories.MessageRepository;
import main.repositories.UserRepository;
import main.response.AddMessageResponse;
import main.response.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

@RestController
public class ChatController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageRepository messageRepository;

    private static final SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");

    @GetMapping(path = "/api/auth")
    public AuthResponse auth() {
        AuthResponse response = new AuthResponse();
        String getSessionId = getSessionId();
        User user = userRepository.getBySessionId(getSessionId);
        response.setResult(user != null);
        if(user!=null){
            response.setName(user.getName());
        }
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

    @PostMapping(path = "/api/messages")
    public AddMessageResponse addMessage(HttpServletRequest httpServletRequest){
        String text = httpServletRequest.getParameter("text");
        String sessionId = getSessionId();
        User user = userRepository.getBySessionId(sessionId);
        Date time = new Date();
        Message message = new Message();
        message.setTime(time);
        message.setMessage(text);
        message.setUser(user);
        messageRepository.save(message);

        AddMessageResponse response = new AddMessageResponse();
        response.setResult(true);
        response.setTime(formatter.format(time));
        return response;
    }
}
