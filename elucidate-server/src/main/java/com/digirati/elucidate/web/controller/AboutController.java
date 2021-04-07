package com.digirati.elucidate.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController(AboutController.CONTROLLER_NAME)
public class AboutController {

  public static final String CONTROLLER_NAME = "aboutController";

  @GetMapping("/about")
  public ResponseEntity<String> get() {
    return ResponseEntity.ok("about me");
  }

}
