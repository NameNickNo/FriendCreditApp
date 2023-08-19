package com.friend.app.controllers.soap;

import com.friend.app.service.FriendshipService;
import com.friend.app.setting.HibernateQualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/friendship")
public class FriendshipController {

    private final FriendshipService friendshipService;

    public FriendshipController(@HibernateQualifier FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    @PostMapping("/create/{id}")
    public String createFriendship(@PathVariable("id") int id) {
        friendshipService.createFriendship(id);
        return "redirect:/person";
    }
}
