package wad.library.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {
    
    @RequestMapping(value="login", method=RequestMethod.GET)
    public String getLogin(){
        System.out.println("GET to login");
        return "login";
    }
    
    @RequestMapping(value="loginfailed", method=RequestMethod.GET)
    public String getLoginFailed(Model model){
        System.out.println("GET to loginfailed");
        model.addAttribute("error", true);
        return "login";
    }
    
    @RequestMapping(value="logout", method=RequestMethod.GET)
    public String getLogout(Model model){
        return "redirect:/app";
    }
        
}
