package me.soulyana.truecalling.repositories;

import me.soulyana.truecalling.models.EdAchievement;
import me.soulyana.truecalling.models.Person;
import org.springframework.data.repository.CrudRepository;

import java.util.LinkedHashSet;
import java.util.Set;

public interface EdAchievementRepository extends CrudRepository<EdAchievement, Long> {
    LinkedHashSet<EdAchievement> findAllBySchool(String school);

    Set<EdAchievement> findAllByPerson(Person person);

    long countAllByPerson(Person person);


}
