package com.relax.relax.controller;

import com.relax.relax.common.annotation.RelaxClass;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RelaxClass(value = "dept")
@RestController
@RequestMapping("/dept")
public class DeptController {
}
