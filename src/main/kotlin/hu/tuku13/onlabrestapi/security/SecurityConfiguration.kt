package hu.tuku13.onlabrestapi.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import javax.sql.DataSource

@EnableWebSecurity
class SecurityConfiguration : WebSecurityConfigurerAdapter() {
    @Autowired
    private lateinit var dataSource: DataSource

    @Autowired
    private lateinit var jwtRequestFilter: JwtRequestFilter

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.jdbcAuthentication()
            ?.dataSource(dataSource)
            ?.usersByUsernameQuery("select name, hashed_password, enabled\n" +
                    "from accounts\n" +
                    "inner join users u on u.user_id = accounts.user_id\n" +
                    "where name = ?")
            ?.authoritiesByUsernameQuery("select username, authority\n" +
                    "from authorities\n" +
                    "where username = ?")
    }

    override fun configure(http: HttpSecurity?) {
        if (http == null) return

        http.csrf().disable()
            .authorizeRequests()
            .mvcMatchers("/login").permitAll()
            .mvcMatchers("/register").permitAll()
            .anyRequest().authenticated()
            .and().sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter::class.java)
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = NoOpPasswordEncoder.getInstance()
}