package org.backend.wrapper.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @GetMapping("/")
    public String hello()
    {
        return "Hello to new Backend Service";
    }

    @GetMapping("/another")
    public String anotherHello()
    {
        return "Another Hello to new Backend Service";
    }

    @GetMapping("/another-one")
    public String anotherOneHello()
    {
        return "Another One Hello to new Backend Service";
    }

}  