package com.digirati.elucidate.web.controller.about;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AboutController {
  public static final String CONTROLLER_NAME = "aboutController";

  static class About {
    String value = "This is text about this server";
  }

  @GetMapping(value = "/about")
  public About getAbout() {
    return new About();
  }

}
