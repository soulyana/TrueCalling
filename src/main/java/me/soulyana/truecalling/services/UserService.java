package me.soulyana.truecalling.services;

import me.soulyana.truecalling.models.Person;
import me.soulyana.truecalling.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    PersonRepository personRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    EdAchievementRepository edAchievementRepository;

    @Autowired
    SkillRepository skillRepository;

    @Autowired
    WorkExpRepository workExpRepository;

    @Autowired
    UserService userService;

    @Autowired
    public UserService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person findByUsername(String username) {
        return personRepository.findByUsername(username);
    }

    public Person findByEmail(String email) {
        return personRepository.findByEmail(email);
    }

    public long countByEmail(String email) {
        return personRepository.countByEmail(email);
    }

    public void saveRecruiter(Person person){
        person.addRole(roleRepository.findByRole("RECRUITER"));
        person.setEnabled(true);
        personRepository.save(person);
    }
    public void saveJobSeeker(Person person){
        person.addRole(roleRepository.findByRole("JOB_SEEKER"));
        person.setEnabled(true);
        personRepository.save(person);
    }
}