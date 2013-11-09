/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wad.library.domain;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;


/**
 * A user's role, representing his ability to do things on the website.
 * @author Loezi
 */
@Entity(name = "ROLES")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    
    private String rolename;
    
    @ManyToMany(mappedBy = "roles")
    private List<User> users;

    /**
     * @return Role's ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets a new ID for this role.
     * @param id the new ID.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return Role's name
     */
    public String getRolename() {
        return rolename;
    }

    /**
     * Sets a new name for this role.
     * @param rolename the new name.
     */
    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    /**
     * Lists all the users with this role.
     * @return List of users with this role.
     */
    public List<User> getUsers() {
        return users;
    }

    /**
     * Sets the list of users with this role.
     * @param users The new list of users.
     */
    public void setUsers(List<User> users) {
        this.users = users;
    }
    
    
}
