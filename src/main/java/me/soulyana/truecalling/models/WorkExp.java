package me.soulyana.truecalling.models;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
public class WorkExp {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotEmpty
    @Size(min = 2, max = 50)
    private String jobTitle;

    @NotEmpty
    @Size(min = 2, max = 50)
    private String companyName;

    //Use @Temporal for date validation
    @NotNull
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "MMM, yyyy")
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "MMM, yyyy")
    private Date endDate;

    @NotEmpty
    @Size(min = 2, max = 50)
    private String duty;

    //do i need more than one duty?
//    @Size(min = 2, max = 50)
//    private String duty1;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "PERSON_ID")
    private Person person;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
