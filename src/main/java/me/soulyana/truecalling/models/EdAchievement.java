package me.soulyana.truecalling.models;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
public class EdAchievement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotEmpty
    @Size(min = 2, max = 50)
    private String degree;

    @NotEmpty
    @Size(min = 2, max = 50)
    private String major;

    @NotEmpty
    @Size(min = 2, max = 50)
    private String school;

    @NotNull
    @DateTimeFormat(pattern = "yyyy")
    private Date gradYear;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "PERSON_ID")
    private Person person;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public Date getGradYear() {
        return gradYear;
    }

    public void setGradYear(Date gradYear) {
        this.gradYear = gradYear;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
