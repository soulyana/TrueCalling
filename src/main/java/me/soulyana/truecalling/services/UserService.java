package me.soulyana.truecalling.services;

import me.soulyana.truecalling.models.Person;
import me.soulyana.truecalling.repositories.PersonRepository;
import me.soulyana.truecalling.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    PersonRepository personRepository;

    @Autowired
    RoleRepository roleRepository;

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
        person.addRole(roleRepository.findByRole("ROLE_RECRUITER"));
        person.setEnabled(true);
        personRepository.save(person);
    }
    public void saveJobSeeker(Person person){
       person.addRole(roleRepository.findByRole("ROLE_JOB_SEEKER"));
        person.setEnabled(true);
        personRepository.save(person);
    }
}