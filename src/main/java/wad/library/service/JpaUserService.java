/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wad.library.service;

import java.util.ArrayList;
import java.util.Collection;
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

@Service
public class JpaUserService implements UserDetailsService, UserService{

    @Autowired
    private UserRepository userRepository;
    
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

    @Override
    public boolean userExits(String username) {
        return (userRepository.findByUsername(username) != null);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

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
    
    @Override
    public void deleteUser(Long id){
        userRepository.delete(id);
    }
}
