package com.learn.google.authenticator.controller;


import com.learn.google.authenticator.model.User;
import com.learn.google.authenticator.service.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/2fa")
public class TOTPController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/users")
    public @ResponseBody
    User createUser(@RequestBody User user) {
        User savedUser = userService.createUser(user);
        savedUser.setPassword("");
        return savedUser;
    }

    @GetMapping(value = "/qrcode/get/{username}")
    public String generateQRCode(@PathVariable("username") String userName) throws Throwable {
        String otpProtocol = userService.generateOTPProtocol(userName);
        System.out.println(otpProtocol);
        return userService.generateQRCode(otpProtocol);
    }

    @PostMapping(value = "/qrcode/validate/{username}")
    public boolean validateTotp(@PathVariable("username") String userName, @RequestBody String requestJson) {
        JSONObject json = new JSONObject(requestJson);
        return userService.validateTotp(userName, Integer.parseInt(json.getString("totpKey")));
    }

}
