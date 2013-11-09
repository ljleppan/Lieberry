/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wad.library.domain;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Represents an useraccount for the website.
 * @author ljleppan
 */
@Entity(name = "USERS")
public class User implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    
    @NotBlank
    @Column(unique=true, name="USER")
    private String username;
    
    @NotBlank
    @Column(name="PASSWORD")
    private String password;
    
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Role> roles;

    /**
     * @return the user's ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets a new ID for the user.
     * @param id the new ID.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the user's username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets a new username for the user.
     * @param username the new username.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the user's password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets a new password for the user.
     * @param password the new password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return List of roles this user has.
     */
    public List<Role> getRoles() {
        return roles;
    }

    /**
     * Set's this users roles to a new list of roles.
     * @param roles the new list of roles.
     */
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
    
    
}
