package me.soulyana.truecalling.repositories;

import me.soulyana.truecalling.models.Person;
import me.soulyana.truecalling.models.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findByRole(String role);

    Set<Role> findAllByPersonSet(Person person);
}
