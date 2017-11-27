package me.soulyana.truecalling.controllers;


import me.soulyana.truecalling.PredeterminedSkills;
import me.soulyana.truecalling.models.EdAchievement;
import me.soulyana.truecalling.models.Person;
import me.soulyana.truecalling.models.Role;
import me.soulyana.truecalling.models.Skill;
import me.soulyana.truecalling.repositories.*;
import me.soulyana.truecalling.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class MainController {

    @Autowired
    EdAchievementRepository edAchievementRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    SkillRepository skillRepository;

    @Autowired
    WorkExpRepository workExpRepository;

    @Autowired
    UserService userService;


    @GetMapping("/search")
    public String searchGet(Model model, Principal principal) {

        // display the appropriate navbar depending on the logged in persons role
        if(personRepo.findByUsername(principal.getName()).getRole().equals("ROLE_USER")) {
            model.addAttribute("navType", "user");
        }
        else {
            model.addAttribute("navType", "recruiter");
        }
        model.addAttribute("pageState", getPageLinkState(personRepo.findByUsername(principal.getName())));

        return "search";
    }


    /*******************************home Page , default home page and Login***********************************************/

    @RequestMapping("/")
    public String showHomePages(Model model) {

        if (roleRepository.count() == 0) {
            Role jobSeekerRole = new Role();
            jobSeekerRole.setRole("ROLE_USER");
            roleRepository.save(jobSeekerRole);

            Role recruiterRole = new Role();
            recruiterRole.setRole("ROLE_RECRUITER");
            roleRepository.save(recruiterRole);

        }

        model.addAttribute("rowNumberP", personRepository.count());
        model.addAttribute("rowNumberE", edAchievementRepository.count());
        model.addAttribute("rowNumberS", skillRepository.count());
        model.addAttribute("rowNumberX", workExpRepository.count());

        model.addAttribute("listRoles", roleRepository.findAll());
        model.addAttribute("allUser", personRepository.findAll());
        model.addAttribute("searchEdu", edAchievementRepository.findAll());
        model.addAttribute("searchExp", workExpRepository.findAll());
        model.addAttribute("searchSkill", skillRepository.findAll());

        // create a list of skills
        if (skillRepository.count() == 0) {
            for (int i = 0; i < PredeterminedSkills.skills.length; i++) {
                Skill sExpert = new Skill();
                Skill sProficient = new Skill();
                Skill sBeginner = new Skill();
                sExpert.setSkill(PredeterminedSkills.skills[i]);
                sExpert.setSkillRanking("Expert");
                sProficient.setSkill(PredeterminedSkills.skills[i]);
                sProficient.setSkillRanking("Proficient");
                sBeginner.setSkill(PredeterminedSkills.skills[i]);
                sBeginner.setSkillRanking("Familiar");
                skillRepository.save(sExpert);
                skillRepository.save(sProficient);
                skillRepository.save(sBeginner);
            }
        }
        return "redirect:/login";
    }

        @RequestMapping("/login")
        public String login() {
            return "login";
        }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("message", "Register New User or Login");
        model.addAttribute("numRecruiters", personRepository.countByRoles(roleRepository.findByRole("ROLE_RECRUITER")));
        model.addAttribute("numJobSeekers", personRepository.countByRoles(roleRepository.findByRole("ROLE_USER")));
        // after successfully logging in, user will see their summary page via the /summary route
        // there is no login post route, it is never called, SecurityConfiguration class sets the default route
        // after logging in
        return "login";
    }

        @GetMapping("/index")
        public String showHomePage() {
            return "index";
        }

        /************************************User Info to add user to modify the existing and to delete the existing user information *********************************************************************/

        /*This method is used to dispaly a form of person info for a user to enter values*/
        @GetMapping("/registrationForm")
        public String addUserInfo(Model model) {
            model.addAttribute("newUser", new Person());
            model.addAttribute("listRoles", roleRepository.findAll());
            return "registrationForm";
        }
    /*This method is used to check the validation for each values has been entered and if it is valid data then it will save it to person table
    * also store and check  the record of the row number in data base table*/
        @PostMapping("/registrationForm")
        public String addUserInfo(@Valid @ModelAttribute("newUser") Person person, BindingResult bindingResult,
                                  Model model){
            model.addAttribute("newUser", person);
            if (bindingResult.hasErrors()) {

                return "registrationForm";
            }
            else if(person.getRole().equalsIgnoreCase("ROLE_USER"))      {

                userService.saveJobSeeker(person);
                model.addAttribute("message","User Account Successfully Created");
            }
            else if (person.getRole().equalsIgnoreCase("ROLE_RECRUITER"))        {

                userService.saveRecruiter(person);
                model.addAttribute("message","Recruiter Account Successfully Created");
            }

            model.addAttribute("message", "Thank you for registering "
                    + person.getFirstName() + " " + person.getLastName() + ", please log in");


            model.addAttribute("numRecruiters", personRepository.countByRoles(roleRepository.findByRole("ROLE_RECRUITER")));
            model.addAttribute("numSeekers", personRepository.countByRoles(roleRepository.findByRole("ROLE_USER")));
            return "editRegistrationForm";
        }


    /*This method is used to modify the existing in forms then update data bas tables according to there modify fields */
        @RequestMapping("/updateUserInfo/{id}")
        public String updateUserInfo(@PathVariable("id") long id, Model model){
            model.addAttribute("newUser", personRepository.findOne(id));
            model.addAttribute("listRoles",roleRepository.findAll());
            return "registrationForm";
        }
    /*This method is used to delete the existing data  records from data base table and dispaly the rest of data which has been there*/
        @RequestMapping("/deleteUserInfo/{id}")
        public String delUserInfo(@PathVariable("id") long id){
            personRepository.delete(id);
            return "redirect:/listUserInfo";
        }
    /*This method is used to display the existing data  records from data base table*/
        @RequestMapping("/listUserInfo")
        public String listUserInfo(Model model){
            model.addAttribute("searchUser", personRepository.findAll());
            return "listUserInfo";
        }
        /******************Education Information to add education to modify the existing and to delete the existing education information  *******************************************************************/
  /*This method is used to dispaly a form of  education achievement to person to enter values*/
        @GetMapping("/addEduInfo")
        public String addEducationInfo(EdAchievement edAchievement, Principal principal, Model model) {
         /*Here we allow the user only has to enter 10 most recent education achivement information and
	    if the user or person tries to enter more than 10 information then submit button will get
	    disable so that they cannot enter more than ten information*/
            model.addAttribute("disSubmit", edAchievementRepository.count() >= 10);
            model.addAttribute("rowNumber", edAchievementRepository.count());
            Person person = personRepository.findByUsername(principal.getName());
            edAchievement.setPerson(person);
            model.addAttribute("newEduInfo", edAchievement);
            return "addEduInfo";
        }

    /*This method is used to check the validation for each values has been entered and if it is valid data then it will save it to data base table
   * also store and check  the record of the rows in data base table*/
        @PostMapping("/addEduInfo")
        public String addEducationInfo(@Valid @ModelAttribute("newEduInfo") EduAchievements eduAchievements,BindingResult bindingResult,Model model) {

            if (bindingResult.hasErrors()) {
                // expect at least one educational info
                model.addAttribute("rowNumber", eduAchievementsRepostory.count());
                return "addEduInfo";
            }

            eduAchievementsRepostory.save(eduAchievements);
            model.addAttribute("rowNumber", eduAchievementsRepostory.count());
            return "redirect:/index";

        }

    /*This method is used to modify the existing in forms then update data bas tables according to there modify fields */
        @RequestMapping("/updateEduInfo/{id}")
        public String updateEduInfo(@PathVariable("id") long id, Model model){

            model.addAttribute("newEduInfo", eduAchievementsRepostory.findOne(id));
            return "addEduInfo";
        }

    /*This method is used to delete the existing data  records from data base table and dispaly the rest of data which has been there*/
        @RequestMapping("/deleteEduInfo/{id}")
        public String delEduInfo(@PathVariable("id") long id){
            eduAchievementsRepostory.delete(id);
            return "redirect:/listEduInfo";
        }
    /*This method is used to display the existing data  records from data base table*/
        @RequestMapping("/listEduInfo")
        public String listEduInfo(Model model){
            model.addAttribute("searchEdu", eduAchievementsRepostory.findAll());
            return "listEduInfo";
        }
        /***********************************************************************************************************************/
        /*********************************************Work Experiences Information to add *Work Experiences to modify the existing and to delete the existing *Work Experiences information *********************************************************/
    /*This method is used to dispaly a form of  work Experiences to person to enter values*/
        @GetMapping("/addWorkExpInfo")
        public String addWorkExpiInfo(WorkExperiences workExperiences,Principal principal,Model model) {
          /*Here we allow the user only has to enter 10 most recent work experience information and
	    if the user or person tries to enter more than 10 information then submit button will get
	    disable so that they cannot enter more than ten information*/

            Resume resume=resumeRepostory.findByUsername(principal.getName());
            workExperiences.setResume(resume);
            model.addAttribute("disSubmit", workExperiencesRepostory.count() >= 10);
            model.addAttribute("rowNumber", workExperiencesRepostory.count());
            model.addAttribute("newWork",workExperiences);
            return "addWorkExpInfo";
        }

    /*This method is used to check the validation for each values has been entered and if it is valid data then it will save it to data base table
    * also store and check  the record of the rows in data base table*/
        @PostMapping("/addWorkExpInfo")
        public String addWorkExpiInfo(@Valid @ModelAttribute("newWork") WorkExperiences workExperiences,BindingResult bindingResult,Model model) {

            if (bindingResult.hasErrors()) {
                return "addWorkExpInfo";
            }

            workExperiencesRepostory.save(workExperiences);
            model.addAttribute("rowNumber", workExperiencesRepostory.count());
            return "redirect:/index";
        }
    /*This method is used to modify the existing in forms then update data bas tables according to there modify fields */
        @RequestMapping("/updateExpInfo/{id}")
        public String updateWorkExp(@PathVariable("id") long id, Model model){
            model.addAttribute("newWork", workExperiencesRepostory.findOne(id));
            return "addWorkExpInfo";
        }
    /*This method is used to delete the existing data  records from data base table and dispaly the rest of data which has been there*/
        @RequestMapping("/deleteExpInfo/{id}")
        public String delWorkExpInfo(@PathVariable("id") long id){
            workExperiencesRepostory.delete(id);
            return "redirect:/listExpInfo";
        }
    /*This method is used to display the existing data  records from data base table*/
        @RequestMapping("/listExpInfo")
        public String listWorkExpInfo(Model model){
            model.addAttribute("searchExp", workExperiencesRepostory.findAll());
            return "listExpInfo";
        }
        /***************************************************************************************************************************/

        /***********************************************Skills Information to add Skills to modify the existing and to delete the existing Skills information****************************************************************************/
   /*This method is used to dispaly a form of  skills to person to enter values*/
        @GetMapping("/addSkillInfo")
        public String addSkilsInfo( Skills skills,Principal principal,Model model) {
        /*Here we allow the user only has to enter 20 skills information and
	    if the user or person tries to enter more than 20 information then submit button will get
	    disable so that they cannot enter more than ten information*/
            Resume resume=resumeRepostory.findByUsername(principal.getName());
            skills.setResume(resume);
            model.addAttribute("disSubmit", skillsRepostory.count() >= 20);
            model.addAttribute("rowNumber", skillsRepostory.count());
            model.addAttribute("newSkill", skills);
            return "addSkillInfo";
        }
    /*This method is used to check the validation for each values has been entered and if it is valid data then it will save it to data base table
     * also store and check  the record of the rows in data base table*/
        @PostMapping("/addSkillInfo")
        public String addSkilsInfo(@Valid @ModelAttribute("newSkill") Skills skills,BindingResult bindingResult,Model model) {

            if (bindingResult.hasErrors()) {
                model.addAttribute("rowNumber", skillsRepostory.count());
                return "addSkillInfo";
            }

            skillsRepostory.save(skills);
            model.addAttribute("rowNumber", skillsRepostory.count());
            return "redirect:/index";
        }

    /*This method is used to modify the existing in forms then update data bas tables according to there modify fields */
        @RequestMapping("/updateSkillInfo/{id}")
        public String updateSkillInfo(@PathVariable("id") long id, Model model){
            model.addAttribute("newSkill", skillsRepostory.findOne(id));
            return "addSkillInfo";
        }
    /*This method is used to delete the existing data  records from data base table and dispaly the rest of data which has been there*/
        @RequestMapping("/deleteSkillInfo/{id}")
        public String delSkillInfo(@PathVariable("id") long id){
            skillsRepostory.delete(id);
            return "redirect:/listSkillInfo";
        }
    /*This method is used to display the existing data  records from data base table*/
        @RequestMapping("/listSkillInfo")
        public String listSkillInfo(Model model){
            model.addAttribute("searchSkill", skillsRepostory.findAll());
            return "listSkillInfo";
        }

        /*******************************Result Info***************************************************************/

        @RequestMapping("/EditResumedetail")
        public String viewResume(Principal principal, Model model) {
            Resume resumeR=resumeRepostory.findByUsername(principal.getName());
            model.addAttribute("resumeR", resumeR);
            model.addAttribute("listEdu",eduAchievementsRepostory.findByResume(resumeR));
            model.addAttribute("listSkill",skillsRepostory.findByResume(resumeR));
            model.addAttribute("listExps",workExperiencesRepostory.findByResume(resumeR));
            return "EditResumedetail";
        }

        /*******************************************************************************************/
        @GetMapping("/SummerizedResume")
        public String summary(Principal principal,Model model) {
            Resume resumeR=resumeRepostory.findByUsername(principal.getName());
            model.addAttribute("resumeR", resumeR);
            model.addAttribute("listEdu",eduAchievementsRepostory.findByResume(resumeR));
            model.addAttribute("listSkill",skillsRepostory.findByResume(resumeR));
            model.addAttribute("listExps",workExperiencesRepostory.findByResume(resumeR));
            return "SummerizedResume";
        }
        //Search a particular person and display the resume
        @GetMapping("/searchPeoples")
        public String searchPeople(Principal principal,Model model) {
            model.addAttribute("newuser",new Resume());
            return "searchPeoples";
        }
        @PostMapping("/searchPeoples")
        public String searchPeople(@ModelAttribute("newuser") Resume resumes,Model model) {
            Iterable<Resume>resumeIterable=resumeRepostory.findAllByFirstname(resumes.getFirstname());
            model.addAttribute("dipUser",resumeIterable);
            return "dispPeopleInfo";
        }
        @GetMapping("/searchSchool")
        public String searchSchool(Model model) {
            model.addAttribute("newEdu",new EduAchievements());
            return "searchSchool";
        }
        @PostMapping("/searchSchool")
        public String searchSchool(@ModelAttribute("newEdu") EduAchievements eduAchievements,Model model) {
            Iterable<EduAchievements>listedu=eduAchievementsRepostory.findByUniName(eduAchievements.getUniName());
            model.addAttribute("eduList",listedu);
            return "dispSchoolInfo";
        }
        @GetMapping("/searchCompany")
        public String searchCompany(Model model) {
            model.addAttribute("newExps",new WorkExperiences());
            return "searchCompany";
        }
        @PostMapping("/searchCompany")
        public String searchCompany(@ModelAttribute("newExps") WorkExperiences workExperiences,Model model) {
            Iterable<WorkExperiences>listorg=workExperiencesRepostory.findByOrgName(workExperiences.getOrgName());
            model.addAttribute("workList",listorg);
            return "dispCompanyInfo";
        }
        //To see for the particular job title deatils
        @GetMapping("/listJobs")
        public String listJobs(Model model)    {
            model.addAttribute("listjob",jobRepository.findAll());
            return"listJobInfo";
        }
        @GetMapping("/jobDetailInfo/{id}")
        public String jobDetail(Model model)    {
            model.addAttribute("listjob",jobRepository.findAll());
            return"jobDetailInfo";
        }
        //To see for the particular job title details of employer information
        @GetMapping("/searchJobsByComp")
        public String searchEmployer(Model model) {
            model.addAttribute("listJob", new Job());
            return "searchJobsByComp";
        }
        @PostMapping("/searchJobsByComp")
        public String searchEmployer(@ModelAttribute("listJob") Job jobs,Model model){
            Iterable<Job>jobIterable=jobRepository.findByEmployer(jobs.getEmployer());
            model.addAttribute("listJob",jobIterable);
            return "dispEmpJobDetail";
        }
        @GetMapping("/searchJobsByTitle")
        public String searchJobs(Model model) {
            model.addAttribute("listJob", new Job());
            return "searchJobsByTitle";
        }
        @PostMapping("/searchJobsByTitle")
        public String searchJobs(@ModelAttribute("listJob") Job jobs,Model model){
            Iterable<Job>jobIterable=jobRepository.findByTitle(jobs.getTitle());
            model.addAttribute("listJob",jobIterable);
            return "dispEmpJobDetail";
        }

        @GetMapping("/skillNotifications")
        public String skillMatching(Principal principal, Model model) {
            Resume resume= resumeRepostory.findByUsername(principal.getName());
            Iterable<Job> jobList = jobRepository.findAll();
            Set<Job> jobSet=new HashSet<>();
            if (resume.getSkillsSet().isEmpty())
            {
                model.addAttribute("message", "Enter your skills!");
                return "message";
            }

            else {

                for (Job jobs : jobList) {
                    for (Skills jobSkill : jobs.getJobskill()) {
                        for (Skills resumeSkill : resume.getSkillsSet()) {
                            if (jobSkill.getSkill().equals(resumeSkill.getSkill())) {
                                jobSet.add(jobs);
                            } else {
                                System.out.println("no Job found");
                            }

                        }
                    }
                }
            }
            model.addAttribute("message", "Some jobs are match your skill");
            model.addAttribute("joblist", jobSet);
            return "skillNotifications";
        }
    }
}
