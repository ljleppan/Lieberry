/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wad.library.service;

import java.util.ArrayList;
import java.util.Collection;
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
public class UserService implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;
    
    @PostConstruct
    public void init(){
        if (userRepository.count() <= 0){
            User admin = new User();
            admin.setUsername("a");
            admin.setPassword("a");
            admin.setEmail("example@example.com");
            List<Role> roles = new ArrayList<Role>();
            Role role = new Role();
            role.setRolename("admin");
            roles.add(role);
            admin.setRoles(roles);
            
            User user = new User();
            user.setUsername("mockUserForSeleniumThisIsBad");
            user.setPassword("h63Eaa6Eq58X92IBccDFmYN9B1i0o7wtCwfOtEimmq2vqLKSzkU6gmpterjNpfLp");
            user.setEmail("example2@example.com");
            roles = new ArrayList<Role>();
            role = new Role();
            role.setRolename("user");
            roles.add(role);
            user.setRoles(roles);
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

}
