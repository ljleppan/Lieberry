package wad.library.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import wad.library.service.UserService;

/**
 * Controller for security and user management related requests.
 * @author Loezi
 */
@Controller
public class SecurityController {
    
    @Autowired
    private UserService userService;
    
    /**
     * Return the "register" view
     * 
     * <p>Security: Logged in.</p>
     * 
     * @return "register".
     */
    @RequestMapping(value="register", method=RequestMethod.GET)
    public String getRegisterForm(){
        System.out.println("GET to register");
        return "register";
    }
    
    /**
     * Return the "login" view.
     * 
     * <p>Security: Anonymous.</p>
     * 
     * @return "login".
     */
    @RequestMapping(value="login", method=RequestMethod.GET)
    public String getLogin(){
        System.out.println("GET to login");
        userService.init();
        return "login";
    }
    
    /**
     * Return the "login" view after a failed login attempt.
     * 
     * <p>Returned view contains an error message about invalid credetials.</p>
     * 
     * <p>Security: Anonymous.</p>
     * 
     * @param model Instance of Model for given HTTP request and related response.
     * @return "login".
     */
    @RequestMapping(value="loginfailed", method=RequestMethod.GET)
    public String getLoginFailed(Model model){
        System.out.println("GET to loginfailed");
        model.addAttribute("error", true);
        return "login";
    }
    
    /**
     * Returns the main menu after logging the user out.
     * 
     * <p>Security: Anonymous.</p>
     * 
     * @param model Instance of Model for given HTTP request and related response.
     * @return Webapp root.
     */
    @RequestMapping(value="logout", method=RequestMethod.GET)
    public String getLogout(Model model){
        return "redirect:/app";
    }
        
}
