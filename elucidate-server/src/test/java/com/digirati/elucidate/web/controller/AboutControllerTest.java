package com.digirati.elucidate.web.controller;

import org.junit.*;
import org.springframework.http.*;
import org.springframework.test.web.servlet.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


public class AboutControllerTest extends AbstractTest {

  @Test
  public void getAbout() throws Exception {
    String uri = "/about";
    MvcResult mvcResult = mvc.perform(get(uri)
        .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

    int status = mvcResult.getResponse().getStatus();
    assertEquals(200, status);
    String content = mvcResult.getResponse().getContentAsString();
    assertEquals("about me", content);
  }
}