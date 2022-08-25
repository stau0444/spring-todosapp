package com.ugo.todoapp.web;

import com.ugo.todoapp.core.todos.application.TodoFinder;
import com.ugo.todoapp.core.todos.domain.Todo;
import com.ugo.todoapp.web.convert.TodoToSpreadsheetConverter;
import com.ugo.todoapp.web.model.SiteProperties;
import org.apache.catalina.filters.SetCharacterEncodingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.AbstractView;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Controller
@RequestMapping("/todos")
public class TodoController {

    private final SiteProperties siteProperties;
    private final TodoFinder finder;
    public TodoController(SiteProperties siteProperties,TodoFinder todoFinder) {
        this.siteProperties = siteProperties;
        this.finder = todoFinder;
    }

    @ModelAttribute("site")
    public SiteProperties siteProperties(){
        return new SiteProperties();
    }
    @RequestMapping("")
    public void todos(){

    }

    @RequestMapping( produces = "text/csv")
    public void downloadTodos(Model model){
        model.addAttribute(
                "todos",
                new TodoToSpreadsheetConverter()
                        .convert(finder.getAll())
        );
    }


    public static class TodoCsvView extends AbstractView implements View{
        //model , request , response를 받아 응답값을 출력한다.
        final Logger logger = LoggerFactory.getLogger(getClass());

        @Override
        protected boolean generatesDownloadContent() {
         return true;
        }

        public TodoCsvView(){
            setContentType("text/csv;");
        }
        @Override
        protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
            logger.info("render model as csv content");
            //response 에 해당 응답이 파일 응답이고 파일명을 알려준다
            response.addHeader(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"todos.csv\"");
            response.setCharacterEncoding("utf-8");
            //파일 첫줄에 목록을 적는다
            response.getWriter().println("id,title,completed");
            
            //모델에서 todo리스트를 받아오고
            List<Todo> todos = (List<Todo>)model.getOrDefault("todos", Collections.emptyList());
            for (Todo todo : todos) {
                // 리스트에 todo객체를  하나씩 읽어 line을 만들고
                String line = String.format("%d,%s,%s",todo.getId(),todo.getTitle(),todo.isCompleted());
                //파일에 적는다.
                response.getWriter().println(line);
            }

            response.flushBuffer();
        }
    }
}

