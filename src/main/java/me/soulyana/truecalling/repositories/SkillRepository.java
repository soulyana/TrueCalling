package me.soulyana.truecalling.repositories;

import me.soulyana.truecalling.models.Person;
import me.soulyana.truecalling.models.Skill;
import org.springframework.data.repository.CrudRepository;

import java.util.LinkedHashSet;
import java.util.Set;

public interface SkillRepository extends CrudRepository<Skill, Long>{

    Skill findBySkillAndSkillRanking(String skill, String skillRanking);

    Set<Skill> findAllByPerson(Person person);

    LinkedHashSet<Skill> findBySkill(String skill);

    long countAllById(long id);
}
