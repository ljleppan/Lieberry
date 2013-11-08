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
    public List<User> getAll();
    public UserDetails loadUserByUsername(String username);
    public User addUser(String username, String password);
    public User toggleAdmin(Long id);
    public boolean userExits(String username);
    public void deleteUser(Long id);
}
