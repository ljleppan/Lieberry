package wad.library.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DefaultController {
    
    @RequestMapping("*")
    public String handleDefault(Model model) {
        boolean loggedIn;
        System.out.println(SecurityContextHolder.getContext().getAuthentication().toString());
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()){
            model.addAttribute("loggedIn", false);
        } else {
            model.addAttribute("loggedIn", false);
        }
        return "menu";
    }
}
