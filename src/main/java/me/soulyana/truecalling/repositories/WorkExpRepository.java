package me.soulyana.truecalling.repositories;

import me.soulyana.truecalling.models.Person;
import me.soulyana.truecalling.models.WorkExp;
import org.springframework.data.repository.CrudRepository;

import java.util.LinkedHashSet;
import java.util.Set;

public interface WorkExpRepository extends CrudRepository<WorkExp, Long> {
    Set<WorkExp> findAllByPerson(Person person);

    long countAllByPerson(Person person);

    LinkedHashSet<WorkExp> findByJobTitle(String jobTitle);

    LinkedHashSet<WorkExp> findByCompanyName(String companyName);
}
