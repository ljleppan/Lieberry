package wad.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wad.library.domain.User;

public interface UserRepository extends JpaRepository<User, Long>{
    public User findByUsername(String username);
}
