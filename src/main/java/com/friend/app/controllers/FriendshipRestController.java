package com.friend.app.controllers;

import com.friend.app.service.FriendshipService;
import com.friend.app.setting.HibernateQualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/friendship")
public class FriendshipRestController {

    private final FriendshipService friendshipService;

    public FriendshipRestController(@HibernateQualifier FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    @PostMapping("/add/{id}")
    public ResponseEntity<HttpStatus> addToFriends(@PathVariable("id") long id) {
        friendshipService.createFriendship(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/remove/{id}")
    public ResponseEntity<HttpStatus> removeFromFriends(@PathVariable("id") long id) {
        friendshipService.removeFriendship(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
