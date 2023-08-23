package com.friend.app.service;

import com.friend.app.dto.PersonChangePasswordDTO;
import com.friend.app.models.person.Person;

public interface PersonChangePasswordService {

    void changePassword(Person person, PersonChangePasswordDTO changePassEntity);
}
