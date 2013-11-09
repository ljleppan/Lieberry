package wad.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wad.library.domain.User;

/**
 * A repository of user data.
 * @author Loezi
 */
public interface UserRepository extends JpaRepository<User, Long>{

    public User findByUsername(String username);
}
