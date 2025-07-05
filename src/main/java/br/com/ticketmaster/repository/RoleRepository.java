package br.com.ticketmaster.repository;

import br.com.ticketmaster.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(Role.RoleName name);
}
