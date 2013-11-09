/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wad.library.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wad.library.domain.Role;
import wad.library.domain.User;
import wad.library.repository.UserRepository;

/**
 *
 * @author ljleppan
 */
@Service
public class JpaUserService implements UserDetailsService, UserService{

    @Autowired
    private UserRepository userRepository;
    
    /**
     * Initializes the repository with some base user acccounts.
     */
    @PostConstruct
    public void init(){
        if (userRepository.count() <= 0){
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("nimda");
            List<Role> roles = new ArrayList<Role>();
            Role role = new Role();
            role.setRolename("admin");
            roles.add(role);
            admin.setRoles(roles);
            userRepository.save(admin);
            
            User user = new User();
            user.setUsername("mockUserForSeleniumThisIsBad");
            user.setPassword("h63Eaa6Eq58X92IBccDFmYN9B1i0o7wtCwfOtEimmq2vqLKSzkU6gmpterjNpfLp");
            roles = new ArrayList<Role>();
            role = new Role();
            role.setRolename("user");
            roles.add(role);
            user.setRoles(roles);
            userRepository.save(user);
        }
    }
    
    /**
     * DO NOT CALL DIRECTLY
     * 
     * Allows Spring to perform logins.
     * 
     * @param username                      User's username
     * @return                              UserDetails of the requested user account.
     * @throws UsernameNotFoundException    If username is not in use.
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                true, true, true, true,
                getAuthorities(user.getRoles()));
    }

    private List<GrantedAuthority> getAuthorities(List<Role> roles) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (Role role: roles){
            authorities.add(new SimpleGrantedAuthority(role.getRolename()));
        }
        return authorities;
    }
    
    /**
     * Adds a new user.
     * @param username New user's username.
     * @param password New user's password.
     * @return         The created User object.
     */
    @Override
    public User addUser(String username, String password){
        if (userRepository.findByUsername(username) != null){
            return null;
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        
        List<Role> roles = new ArrayList<Role>();
        Role role = new Role();
        role.setRolename("user");
        roles.add(role);
        user.setRoles(roles);
        
        userRepository.save(user);
        return user;
    }

    /**
     * Checks if a username is already in use.
     * @param username Username to check.
     * @return         "true" if username is in use, "false" if not.
     */
    @Override
    public boolean userExits(String username) {
        return (userRepository.findByUsername(username) != null);
    }

    /**
     * Returns all the users.
     * @return List of all the known users.
     */
    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    /**
     * Toggles an users admin powers.
     * @param id the ID indetifying the user.
     * @return   the User object corresponding to the changed user, or null.
     */
    @Override
    public User toggleAdmin(Long id) {
        User user = userRepository.findOne(id);
        
        if (user == null){
            return null;
        }
        
        List<Role> roles = user.getRoles();
        boolean isAdmin = false;
        
        for (Role r : roles){
            if (r.getRolename().equals("admin")){
                isAdmin = true;
            }
        }
        
        
        
        if (isAdmin){
            Iterator<Role> iter = roles.iterator();
            while (iter.hasNext()){
                Role r = iter.next();
                if (r.getRolename().equals("admin")){
                    iter.remove();
                }
            }
        } else {
            Role role = new Role();
            role.setRolename("admin");
            roles.add(role);
            user.setRoles(roles);
        }
        
        userRepository.save(user);
        
        return user;
    }
    
    /** 
     * Deletes an user account.
     * @param id the ID of the user account to delete.
     */
    @Override
    public void deleteUser(Long id){
        userRepository.delete(id);
    }
}
