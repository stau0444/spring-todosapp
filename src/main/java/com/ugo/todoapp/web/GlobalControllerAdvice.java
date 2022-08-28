package com.ugo.todoapp.web;

import com.ugo.todoapp.web.model.SiteProperties;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final SiteProperties siteProperties;

    public GlobalControllerAdvice(SiteProperties siteProperties) {
        this.siteProperties = siteProperties;
    }

    @ModelAttribute("site")
    public SiteProperties siteProperties(){
        return siteProperties;
    }
}
