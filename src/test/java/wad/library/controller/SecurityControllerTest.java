package wad.library.controller;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import wad.library.domain.Role;
import wad.library.domain.User;
import wad.library.service.UserService;

public class SecurityControllerTest {
    
    @Mock
    UserService us;
        
    @InjectMocks
    private SecurityController securityController;
    
    User user;
    User admin;
    List<User> users;
    
    MockMvc mockMvc;
    
    private ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();

        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/jsp/");
        viewResolver.setSuffix(".jsp");

        return viewResolver;
    }
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(securityController).setViewResolvers(viewResolver()).build();
        createUsers();
    }
    
    @Test
    public void get_Register() throws Exception{
        mockMvc.perform(get("/register"))
                .andExpect(view().name("register"))
                .andExpect(status().isOk());
        
        verifyNoMoreInteractions(us);
    }
    
    @Test
    public void post_Register_Success() throws Exception{
        when(us.addUser(any(String.class), any(String.class))).thenReturn(user);
        when(us.userExits(any(String.class))).thenReturn(false);
        
        mockMvc.perform(post("/register")
                .param("username", "user")
                .param("password", "up")
                .param("password2", "up"))
                .andExpect(view().name("redirect:/app/registrationsuccess"));
        verify(us, times(1)).addUser(any(String.class), any(String.class));
        verify(us, times(1)).userExits(any(String.class));
        verifyNoMoreInteractions(us);
    }
    
    @Test
    public void post_Register_UsernameExists() throws Exception{
        when(us.addUser(any(String.class), any(String.class))).thenReturn(user);
        when(us.userExits(any(String.class))).thenReturn(true);
        
        mockMvc.perform(post("/register")
                .param("username", "user")
                .param("password", "up")
                .param("password2", "up"))
                .andExpect(view().name("register"))
                .andExpect(model().attribute("usernameError", is("Username is already in use.")));

        verify(us, times(1)).userExits(any(String.class));
        verifyNoMoreInteractions(us);
    }
    
    @Test
    public void post_Register_UsernameIsEmpty() throws Exception{
        when(us.userExits(any(String.class))).thenReturn(false);
        
        mockMvc.perform(post("/register")
                .param("username", "")
                .param("password", "up")
                .param("password2", "up"))
                .andExpect(view().name("register"))
                .andExpect(model().attribute("usernameError", is("Username can not be empty.")));

        verify(us, times(1)).userExits(any(String.class));
        verifyNoMoreInteractions(us);
    }
    
    @Test
    public void post_Register_PasswordIsEmpty() throws Exception{
        when(us.userExits(any(String.class))).thenReturn(false);
        
        mockMvc.perform(post("/register")
                .param("username", "user")
                .param("password", "")
                .param("password2", ""))
                .andExpect(view().name("register"))
                .andExpect(model().attribute("passwordError", is("Must set a password.")));

        verify(us, times(1)).userExits(any(String.class));
        verifyNoMoreInteractions(us);
    }
    
    @Test
    public void post_Register_PasswordsDoNotMatch() throws Exception{
        when(us.userExits(any(String.class))).thenReturn(false);
        
        mockMvc.perform(post("/register")
                .param("username", "user")
                .param("password", "")
                .param("password2", "up"))
                .andExpect(view().name("register"))
                .andExpect(model().attribute("passwordError", is("Passwords did not match.")));

        verify(us, times(1)).userExits(any(String.class));
        verifyNoMoreInteractions(us);
    }
    
    @Test
    public void get_Registrationsuccess() throws Exception{
        mockMvc.perform(get("/registrationsuccess"))
                .andExpect(view().name("login"))
                .andExpect(model().attribute("message", is("Registration Successfull. You can now login.")));
        
        verifyNoMoreInteractions(us);
    }
    
    @Test
    public void get_Login() throws Exception{
        mockMvc.perform(get("/login"))
                .andExpect(view().name("login"));
        
        verifyNoMoreInteractions(us);
    }
    
    @Test
    public void get_Loginfailed() throws Exception{
        mockMvc.perform(get("/loginfailed"))
                .andExpect(view().name("login"))
                .andExpect(model().attribute("error", is(true)));
        
        verifyNoMoreInteractions(us);
    }
    
    @Test
    public void get_Logout() throws Exception{
        mockMvc.perform(get("/logout"))
                .andExpect(view().name("redirect:/app"));
        
        verifyNoMoreInteractions(us);
    }
    
    @Test
    public void get_Users() throws Exception{
        when(us.getAll()).thenReturn(users);
        
        mockMvc.perform(get("/users"))
                .andExpect(view().name("usermanagement"))
                .andExpect(model().attribute("users", is(users)));
        
        verify(us, times(1)).getAll();
        verifyNoMoreInteractions(us);
    }
    
    @Test
    public void post_Users_Id_Admin() throws Exception{
        when(us.toggleAdmin(any(Long.class))).thenReturn(user);
        
        mockMvc.perform(post("/users/1/admin"))
                .andExpect(view().name("redirect:/app/users"));
        
        verify(us, times(1)).toggleAdmin(any(Long.class));
        verifyNoMoreInteractions(us);
    }
    
    @Test
    public void delete_Users_Id() throws Exception{
        mockMvc.perform(delete("/users/1"))
                .andExpect(view().name("redirect:/app/users"));
        
        verify(us, times(1)).deleteUser(any(Long.class));
        verifyNoMoreInteractions(us);
    }
    
    
    private void createUsers(){
        user = new User();
        user.setId((long)1);
        user.setPassword("up");
        user.setUsername("user");
        List<Role> roles = new ArrayList<Role>();
        Role role = new Role();
        role.setId((long)11);
        role.setRolename("user");
        user.setRoles(roles);
        List<User> r_users = new ArrayList<User>();
        r_users.add(user);
        role.setUsers(r_users);
        
        admin = new User();
        admin.setId((long)2);
        admin.setPassword("ap");
        admin.setUsername("admin");
        roles = new ArrayList<Role>();
        role = new Role();
        role.setId((long)12);
        role.setRolename("admin");
        admin.setRoles(roles);
        r_users = new ArrayList<User>();
        r_users.add(admin);
        role.setUsers(r_users);
        
        users = new ArrayList<User>();
        users.add(user);
        users.add(admin);
    }
    
}