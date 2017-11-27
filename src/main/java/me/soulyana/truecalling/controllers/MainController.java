package me.soulyana.truecalling.controllers;


import me.soulyana.truecalling.models.*;
import me.soulyana.truecalling.repositories.*;
import me.soulyana.truecalling.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

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


    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String showRegistrationPage(Model model) {
        model.addAttribute("person", new Person());
        return "registration";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String processRegistrationPage(@Valid @ModelAttribute("person") Person person, BindingResult result, Model model) {
        model.addAttribute("person", person);
        if (result.hasErrors()) {
            return "registration";
        } else if(person.getRole().equalsIgnoreCase("RECRUITER")) {
            userService.saveRecruiter(person);
            model.addAttribute("message", "Recruiter Account Successfully Created");
        } else if(person.getRole().equalsIgnoreCase("JOB_SEEKER")) {
            userService.saveJobSeeker(person);
            model.addAttribute("message", "Job Seeker Account Successfully Created");
        }
        return "editregistration";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/")
    public String showIndex(Model model) {
        model.addAttribute("gotEdAchievement", edAchievementRepository.count());
        model.addAttribute("gotSkill", skillRepository.count());
        model.addAttribute("gotPerson", personRepository.count());
        model.addAttribute("gotRole", roleRepository.count());
        model.addAttribute("gotWorkExp", workExpRepository.count());
        model.addAttribute("personList", personRepository.findAll());
        model.addAttribute("skillList", skillRepository.findAll());
        model.addAttribute("title","Job Database");
        return "index";
    }

    @GetMapping("/addperson")
    public String addPerson(Model model) {
        Person person = new Person();
        model.addAttribute("person", person);
        return "addperson";
    }

    @PostMapping("/addperson")
    public String savePerson(@ModelAttribute("person") Person person) {
        personRepository.save(person);
        return "redirect:/";
    }

    @GetMapping("/addrole")
    public String addRole(Model model) {
        model.addAttribute("role", new Role());
        return "addrole";
    }

    @PostMapping("/addrole")
    public String saveRole(@ModelAttribute("role") Role role) {
        roleRepository.save(role);
        return "redirect:/";
    }

    @GetMapping("/addEdAchievement")
    public String addEdAchievement(EdAchievement edAchievement, Principal principal, Model model) {
        Person person = personRepository.findByUsername(principal.getName());
        edAchievement.setPerson(person);
        model.addAttribute("newEdAchievement", new EdAchievement());
        return "addEdAchievement";
    }

    @PostMapping("/addEdAchievement")
    public String addEdAchievement(@Valid @ModelAttribute("newEdAchievement") EdAchievement edAchievement, BindingResult result) {
        if (result.hasErrors()) {
            return "addEdAchievement";
        }
        edAchievementRepository.save(edAchievement);
        return "redirect:/";
    }

    @GetMapping("/addWorkExp")
    public String addWorkExp(WorkExp workExp, Principal principal, Model model) {
        Person person = personRepository.findByUsername(principal.getName());
        workExp.setPerson(person);
        model.addAttribute("newWorkExp", new WorkExp());
        return "addWorkExp";
    }

    @PostMapping("/addWorkExp")
    public String addWorkExp(@Valid @ModelAttribute("newWorkExp") WorkExp workExp, BindingResult result) {
        if (result.hasErrors()) {
            return "addWorkExp";
        }
        workExpRepository.save(workExp);
        return "redirect:/";
    }

//    @RequestMapping("/detail/{id}")
//    public String showCourse(@PathVariable("id") long id, Model model) {
//        model.addAttribute("course", courseRepository.findOne(id));
//        return "show";
//    }
//
//    @RequestMapping("/update/{id}")
//    public String updateCourse(@PathVariable("id") long id, Model model) {
//        model.addAttribute("course", courseRepository.findOne(id));
//        return "courseform";
//    }
//
//    @RequestMapping("/delete/{id}")
//    public String delCourse(@PathVariable("id") long id){
//        courseRepository.delete(id);
//        return "redirect:/";
//    }
//
//
//    @GetMapping("/search")
//    public String getSearch()
//    {
//        return "coursesearchform";
//    }
//
//    @PostMapping("/search")
//    public String showSearchResults(HttpServletRequest request, Model model)
//    {
//        //Get the search string from the result form
//        String searchString = request.getParameter("search");
//        model.addAttribute("search",searchString);
//        model.addAttribute("courses",courseRepository.findAllByTitleContainingIgnoreCase(searchString));
//        return "list";
//    }
//}


    @RequestMapping("/secure")
    public String secure(HttpServletRequest request, Authentication authentication, Principal principal){
        Boolean isRecruiter = request.isUserInRole("RECRUITER");
        Boolean isJobSeeker = request.isUserInRole("JOB_SEEKER");

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = principal.getName();
        return "secure";
    }
}