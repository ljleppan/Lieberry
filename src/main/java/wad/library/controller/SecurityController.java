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
 * @author ljleppan
 */
@Controller
public class SecurityController {
    
    @Autowired
    UserService userService;
    
    /**
     * DO NOT CALL DIRECTLY
     * 
     * Return the "register" view
     * 
     * <p>Security: Logged in.</p>
     * 
     * @return "register".
     */
    @RequestMapping(value="register", method=RequestMethod.GET)
    public String getRegisterForm(){
        return "register";
    }
    
    /**
     * DO NOT CALL DIRECTLY
     * 
     * Registers a new user account with the provided details.
     * 
     * <p>The provided username must not be already in use and must not be empty.
     * The provided passwords must match and must not be empty.</p>
     * 
     * <p>Security: Anonymous.</p>
     * 
     * @param model     Instance of Model for given HTTP request and related response.
     * @param username  Username for the new user account.
     * @param password  Password for the new user account.
     * @param password2 Password for the new user account.
     * @return          "registrationsuccess" if succesfull, "register" if unsuccesfull.
     */
    @RequestMapping(value="register", method=RequestMethod.POST)
    public String register(Model model,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String password2){  
        boolean error = false;
        if (userService.userExits(username)){
            model.addAttribute("usernameError", "Username is already in use.");
            error = true;
        }
        if (username == null || username.equals("")){
            model.addAttribute("usernameError", "Username can not be empty.");
            error = true;
        }
        if (password == null || password2 == null || password.equals("") || password2.equals("")){
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
    
    /**
     * DO NOT CALL DIRECTLY
     * 
     * Shows the login form with a message about succesfully registering a new user account.
     * 
     * <p>Security: Anonymous.</p>.
     * 
     * @param model Instance of Model for given HTTP request and related response.
     * @return      "login".
     */
    @RequestMapping(value="registrationsuccess", method=RequestMethod.GET)
    public String registrationSuccess(Model model){
        model.addAttribute("message", "Registration Successfull. You can now login.");
        return "login";
    }
    
    /**
     * DO NOT CALL DIRECTLY
     * 
     * Return the "login" view.
     * 
     * <p>Security: Anonymous.</p>
     * 
     * @return "login".
     */
    @RequestMapping(value="login", method=RequestMethod.GET)
    public String getLogin(){
        return "login";
    }
    
    /**
     * DO NOT CALL DIRECTLY
     * 
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
        model.addAttribute("error", true);
        return "login";
    }
    
    /**
     * DO NOT CALL DIRECTLY
     * 
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
    
    /**
     * DO NOT CALL DIRECLY
     * 
     * Shows the user management page.
     * 
     * <p>Security: Admin.</p>
     * 
     * @param model Instance of Model for given HTTP request and related response.
     * @return      "usermanagement"
     */
    @PreAuthorize("hasRole('admin')")
    @RequestMapping(value="users", method=RequestMethod.GET)
    public String getUsers(Model model){
        model.addAttribute("users", userService.getAll());
        return "usermanagement";
    }
    
    /**
     * DO NOT CALL DIRECTLY
     * 
     * Toggles user's admin powers and redirects to the user management page.
     * 
     * <p>Security: Admin</p>
     * 
     * @param model Instance of Model for given HTTP request and related response.
     * @param id    User ID identifying the user whose admin status to toggle
     * @return      The "user management" view.
     */
    @PreAuthorize("hasRole('admin')")
    @RequestMapping(value="users/{id}/admin", method=RequestMethod.POST)
    public String toggleAdmin(Model model, @PathVariable Long id){
        User user = userService.toggleAdmin(id);
        return "redirect:/app/users";
    }
    
    /**
     * DO NOT CALL DIRECTLY
     * 
     * Deletes an user and redirects to the user management page.
     * 
     * <p>Security: Admin.</p>
     * 
     * @param model Instance of Model for given HTTP request and related response.
     * @param id    User ID identifying the user to delete.
     * @return      Redirect to the user management page.
     */
    @PreAuthorize("hasRole('admin')")
    @RequestMapping(value="users/{id}", method=RequestMethod.DELETE)
    public String deleteUser(Model model, @PathVariable Long id){
        userService.deleteUser(id);
        return "redirect:/app/users";
    }
}
