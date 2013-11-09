/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wad.library.service;

import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import wad.library.domain.User;

/**
 *
 * @author ljleppan
 */
public interface UserService {
    
    /**
     * @return All known users.
     */
    public List<User> getAll();
    
    /**
     * DO NOT CALL DIRECTLY
     * 
     * Allows Spring to perform logins.
     * 
     * @param username                      User's username
     * @return                              UserDetails of the requested user account.
     */
    public UserDetails loadUserByUsername(String username);
    
    /**
     * Adds a new user.
     * @param username New user's username.
     * @param password New user's password.
     * @return         The created User object.
     */
    public User addUser(String username, String password);
    
    /**
     * Toggles an users admin powers.
     * @param id the ID indetifying the user.
     * @return   the User object corresponding to the changed user, or null.
     */
    public User toggleAdmin(Long id);
    
    /**
     * Checks if a username is already in use.
     * @param username Username to check.
     * @return         "true" if username is in use, "false" if not.
     */
    public boolean userExits(String username);
    
    /** 
     * Deletes an user account.
     * @param id the ID of the user account to delete.
     */
    public void deleteUser(Long id);
}
