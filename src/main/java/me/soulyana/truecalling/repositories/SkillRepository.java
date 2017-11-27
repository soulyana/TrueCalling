package me.soulyana.truecalling.repositories;

import me.soulyana.truecalling.models.Skill;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SkillRepository extends CrudRepository<Skill, Long>{
    //Does this one work better? Skill findBySkillIsAndSkillRankingIs(String skill, String skillRanking);
    Skill findBySkillAndSkillRanking(String skill, String skillRanking);

    List<Skill> findAllByOrOrderBySkillAsc();

    long countAllById(long id);
}
