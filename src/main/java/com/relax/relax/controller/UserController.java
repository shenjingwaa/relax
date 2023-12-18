package com.relax.relax.controller;

import com.relax.relax.common.annotation.RelaxClass;
import com.relax.relax.domain.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RelaxClass(prefix = "user", entityType = User.class)
@RestController
@RequestMapping("/user")
public class UserController {

}
