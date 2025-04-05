package net.engineeringdigest.CampusApp.config;

import net.engineeringdigest.CampusApp.service.CustomUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
@Configuration
@EnableWebSecurity
public class SpringSecurity extends WebSecurityConfigurerAdapter {
    @Autowired
    private CustomUserDetailsServiceImpl userDetailsService;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/auth/**").permitAll()  // Public routes like signup/login
                .antMatchers("/user/**").authenticated()
                .antMatchers("/admin/**").hasAuthority("admin")
                .antMatchers("/club/create").hasAuthority("Super_user")
                .antMatchers("/club/promotebysuperuser").hasAuthority("Super_user")
                .antMatchers("/club/admin/**").hasAuthority("admin")
               .antMatchers("/club/getclubbyname").hasAnyAuthority("student", "admin", "Super_user")
               .antMatchers("/club/getclubmembers").hasAnyAuthority("student", "admin", "Super_user")// Fix multiple role issue
                .antMatchers("/club/admin/Give_Post_access").hasAuthority("admin")





                .anyRequest().permitAll()
                .and()
                .formLogin()
                .and()
                .httpBasic();

        http.csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    
}
