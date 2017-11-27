package me.soulyana.truecalling.models;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotEmpty
    @Size(min = 2, max = 50)
    private String skill;

    @NotEmpty
    @Size(min = 2, max = 50)
    private String skillRanking;

    @ManyToMany(mappedBy = "skills")
    private Set<Person> person;


    public Skill() {
        person = new HashSet<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getSkillRanking() {
        return skillRanking;
    }

    public void setSkillRanking(String skillRanking) {
        this.skillRanking = skillRanking;
    }

    public Set<Person> getPerson() {
        return person;
    }

    public void setPerson(Set<Person> person) {
        this.person = person;
    }
}
