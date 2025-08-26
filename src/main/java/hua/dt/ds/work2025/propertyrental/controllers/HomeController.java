package hua.dt.ds.work2025.propertyrental.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
    @GetMapping
    public String home(Model model) {

        model.addAttribute("title", "Home");
        //return "page_layout/layout";
        return "home";
    }

    @GetMapping("/home")
    public String homePage() {
        return "home";
    }

}

