package hu.tuku13.onlabrestapi.security

//import org.springframework.beans.factory.annotation.Value
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.security.config.annotation.web.builders.HttpSecurity
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
//import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator
//import org.springframework.security.oauth2.core.OAuth2TokenValidator
//import org.springframework.security.oauth2.jwt.*

//@Configuration
//@EnableWebSecurity
//class SecurityConfig : WebSecurityConfigurerAdapter() {
//
//    @Value("\${auth0.audience}")
//    private val audience: String = String()
//
//    @Value("\${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
//    private val issuer: String = String()
//
//    @Bean
//    fun jwtDecoder() : JwtDecoder {
//        val jwtDecoder = JwtDecoders.fromOidcIssuerLocation(issuer) as NimbusJwtDecoder
//        val audienceValidator: OAuth2TokenValidator<Jwt> = AudienceValidator(audience)
//
//        val withIssuer: OAuth2TokenValidator<Jwt> = JwtValidators.createDefaultWithIssuer(issuer)
//        val withAudience: OAuth2TokenValidator<Jwt> = DelegatingOAuth2TokenValidator(withIssuer, audienceValidator)
//
//        jwtDecoder.setJwtValidator(withAudience)
//        return jwtDecoder
//
//    }
//
//    override fun configure(http: HttpSecurity) {
//        http.authorizeRequests()
//            .mvcMatchers("/login").permitAll()
//            .mvcMatchers("/register").permitAll()
//            .mvcMatchers("/").authenticated()
//            .and()
//            .oauth2ResourceServer().jwt()
//    }
//}