package me.soulyana.truecalling.security;

import me.soulyana.truecalling.repositories.PersonRepository;
import me.soulyana.truecalling.services.SSUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//@Configuration and@EnableWebSecurity This indicates to the compiler that the file is a configuration file and
//        Spring Security is enabled for the application.
//
//        the file class you create (SecurityConfiguration) extends the WebSecurityConflgurerAdapter, which has all of the
//        methods needed to include security in your application.
@Configuration
@EnableWebSecurity
//Prevent cross authitication from make sure its sent from the same browser you are using
//Dosnt include a token that this is me cant veifyCSRF token inside form that authicates you within the browsr
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    SSUserDetailsService userDetailsService;

    @Autowired
    private PersonRepository personRepository;

    @Override
    //Creating a bean to authiticate user and access in spring dont ned to know in depth
    public UserDetailsService userDetailsServiceBean() throws Exception{
        return new SSUserDetailsService(personRepository);
        //Works with Login form
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http
                .authorizeRequests()

                // anyone can access
                .antMatchers("/css/**", "/js/**", "/fonts/**", "/img/**", "/registration", "/")
                .permitAll()

                .antMatchers("/","/registration","/editregistration","/index","/css/**","/js/**","/img/**",
                        "/font-awesome/**","/fonts/**","lib/**", "/homePage")
                .access("hasRole('JOB_SEEKER') or hasRole('RECRUITER')")

                .antMatchers("/addEduInfo","/listEduInfo","/updateEduInfo/**","/addSkillInfo","/listSkillInfo",
                        "/updateSkillInfo/**","/addWorkExpInfo","/listExpInfo","/updateExpInfo/**","/searchPeople",
                        "/searchSchool","/EditResumedetail","/SummerizedResume")
                .access("hasRole('JOB_SEEKER')")

                .antMatchers("/searchPeople", "/searchSchool", "/searchSkill", "/searchCompany")
                .access("hasRole('RECRUITER')")

                .anyRequest().authenticated();


        // login/out
        http
                .formLogin().failureUrl("/login?error") // thymeleaf can conveniently pick up the login errors in the template
                // there is NO post route for /login, I tried and it never gets called, instead the defaultSuccessUrl route gets called
                .defaultSuccessUrl("/summary")
                .and()
                .formLogin().loginPage("/login")
                .permitAll()
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login")
                .permitAll();

        http
                .csrf().disable();

        http
                .headers().frameOptions().disable();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceBean());
    }

}