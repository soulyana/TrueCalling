package me.soulyana.truecalling.models;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Person implements Comparable<Person> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotEmpty
    @Size(min = 2)
    private String firstName;

    @NotEmpty
    @Size(min = 2)
    private String lastName;

    @NotEmpty
    @Column(nullable = false)
    @Email
    @Size(min = 3)
    private String email;

    @NotEmpty
    @Column(name = "username", unique = true)
    private String username;

    @NotEmpty
    private String password;

    private boolean enabled;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<EdAchievement> edAchievements;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<WorkExp> workExps;

    //Use eager here in order to login
    //Role owns person
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(joinColumns = @JoinColumn(name = "PERSON_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
    private Set<Role> roles;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(joinColumns = @JoinColumn(name = "PERSON_ID"),
            inverseJoinColumns = @JoinColumn(name = "SKILL_ID"))
    private Set<Skill> skills;


    //Constructor; use hash set because....
    public Person() {
        this.edAchievements = new HashSet<>();
        this.workExps = new HashSet<>();
        this.roles = new HashSet<>();
        this.skills = new HashSet<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<EdAchievement> getEdAchievements() {
        return edAchievements;
    }

    public void setEdAchievements(Set<EdAchievement> edAchievements) {
        this.edAchievements = edAchievements;
    }

    public Set<WorkExp> getWorkExps() {
        return workExps;
    }

    public void setWorkExps(Set<WorkExp> workExps) {
        this.workExps = workExps;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Skill> getSkills() {
        return skills;
    }

    public void setSkills(Set<Skill> skills) {
        this.skills = skills;
    }

    //Methods to determine roles
    public void addRole(Role role) {
        roles.add(role);
    }

    public void removeRole(Role role) {
        roles.remove(role);
    }

    //Determines only one role per person
    public String getRole() {
        for(Role theRole : roles) {
            if (theRole.getRole().equals("ROLE_RECRUITER")) return "ROLE_RECRUITER";
            if (theRole.getRole().equals("ROLE_USER")) return "ROLE_USER";
        }
        return null;
    }

    //Sort names in ascending order and compare by last name
    @Override
    public int compareTo(Person other) {
        return getLastName().compareToIgnoreCase(other.getLastName());
    }

    public void addSkill(Skill skill) {
        skills.add(skill);
    }

    public void removeSkill(Skill skill) {
        skills.remove(skill);
    }


    // remove edAchievement
    // remove workExp

}
