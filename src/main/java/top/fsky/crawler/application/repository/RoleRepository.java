package top.fsky.crawler.application.repository;


import top.fsky.crawler.application.model.Role;
import top.fsky.crawler.application.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}
