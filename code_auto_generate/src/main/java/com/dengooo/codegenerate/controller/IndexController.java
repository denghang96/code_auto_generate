package com.dengooo.codegenerate.controller;

import com.dengooo.codegenerate.common.resp.RespData;
import com.dengooo.codegenerate.common.vo.AllParams;
import com.dengooo.codegenerate.common.vo.Params;
import com.dengooo.codegenerate.service.IndexService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/")
@Slf4j
public class IndexController {

    @Autowired
    IndexService indexService;

    @GetMapping("index")
    public ModelAndView index(HttpServletRequest request, Model model){
        log.info("=============={}", request.getServletContext().getContextPath());
        return new ModelAndView("index");
    }

    @PostMapping("generate")
    public RespData generate(HttpServletRequest request, @RequestBody AllParams allParams) throws IOException {
        log.info("=============={}", request.getServletContext().getContextPath());
        indexService.generate(allParams);
        return RespData.success();
    }
}
