package com.ugo.todoapp.web;

import com.ugo.todoapp.web.model.SiteProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Controller
@RequestMapping("/todos")
public class TodoController {

    private String siteAuthor;
    private String description;

    public TodoController(
            @Value("${site.author}")String siteAuthor,
            @Value("${site.description}") String description
    ) {
        this.siteAuthor = siteAuthor;
    }

    @RequestMapping("")
    public ModelAndView todos(){

        SiteProperties sp = new SiteProperties();
        sp.setAuthor(siteAuthor);
        sp.setDescription(description);
        ModelAndView mv = new ModelAndView();

        mv.addObject("site" , sp);
        mv.setViewName("todos");

        return mv;
    }
}

