package com.relax.relax.controller;

import com.relax.relax.common.annotation.RelaxClass;
import com.relax.relax.domain.Dept;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RelaxClass(prefix = "dept", entityType = Dept.class)
@RestController
@RequestMapping("/dept")
public class DeptController {
}
