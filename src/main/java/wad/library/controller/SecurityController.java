package wad.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import wad.library.domain.User;
import wad.library.service.UserService;

/**
 * Controller for security and user management related requests.
 * @author Loezi
 */
@Controller
public class SecurityController {
    
    @Autowired
    UserService userService;
    
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
    
    @RequestMapping(value="register", method=RequestMethod.POST)
    public String register(Model model,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String password2){
        
        System.out.println("POST to register");
        
        boolean error = false;
        if (userService.userExits(username)){
            model.addAttribute("usernameError", "Username is already in use.");
            error = true;
        }
        if (password == null || password2 == null){
            model.addAttribute("passwordError", "Must set a password.");
            error = true;
        }
        if (password != null && !password.equals(password2)){
            model.addAttribute("passwordError", "Passwords did not match.");
            error = true;
        }
        
        if (error){
            return "register";
        }
        
        userService.addUser(username, password);
        return "redirect:/app/registrationsuccess";
    }
    
    @RequestMapping(value="registrationsuccess", method=RequestMethod.GET)
    private String registrationSuccess(Model model){
        System.out.println("GET to registrationsuccess");
        model.addAttribute("message", "Registration Successfull. You can now login.");
        return "login";
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
        System.out.println("GET to logout");
        return "redirect:/app";
    }
    
    @PreAuthorize("hasRole('admin')")
    @RequestMapping(value="users", method=RequestMethod.GET)
    public String getUsers(Model model){
        System.out.println("GET to usermanagement");
        model.addAttribute("users", userService.getAll());
        return "usermanagement";
    }
    
    @PreAuthorize("hasRole('admin')")
    @RequestMapping(value="users/{id}/admin", method=RequestMethod.POST)
    public String toggleAdmin(Model model, @PathVariable Long id){
        System.out.println("POST to users/"+id+"/admin");
        User user = userService.toggleAdmin(id);
        return "redirect:/app/users";
    }
    
    @PreAuthorize("hasRole('admin')")
    @RequestMapping(value="users/{id}", method=RequestMethod.DELETE)
    public String deleteUser(Model model, @PathVariable Long id){
        System.out.println("DELETE to users/"+id);
        userService.deleteUser(id);
        return "redirect:/app/users";
    }
}
