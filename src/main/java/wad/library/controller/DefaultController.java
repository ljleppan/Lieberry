package wad.library.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * The default controller.
 * @author Loezi
 */
@Controller
public class DefaultController {
    
    /**
     * Displays the main menu for any requests not matching other mappings.
     * 
     * <p>Security: Anonymous.</p>
     * 
     * @param model Instance of Model for given HTTP request and related response.
     * @return "menu".
     */
    @RequestMapping("*")
    public String handleDefault(Model model) {
        return "menu";
    }
}
