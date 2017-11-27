package me.soulyana.truecalling.repositories;

import me.soulyana.truecalling.models.Person;
import me.soulyana.truecalling.models.Role;
import me.soulyana.truecalling.models.Skill;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public interface PersonRepository extends CrudRepository<Person, Long> {
    Person findByUsername(String username);

    Person findByEmail(String email);

    Set<Person> findAllByUsername(String username);

    Long countByUsername(String username);

    Long countByEmail(String email);

    Long countByRoles(Role role);

    //Find a particular user type with a certain skill. Displays list.
    Collection<Person> findBySkillsIsAndRolesIs(Skill skill, Role role);


}
