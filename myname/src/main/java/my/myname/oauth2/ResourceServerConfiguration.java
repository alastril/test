package my.myname.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;


/**
 * This bean configure for Oauth2, this bean Override {@link WebSecurityConfigurerAdapter} if he configured.
 * The Class ResourceServerConfiguration.
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration  extends ResourceServerConfigurerAdapter{
    
    /** The authentication manager. */
    @Autowired
    @Qualifier("oAuth2AuthenticationManager")
    private AuthenticationManager authenticationManager;

    /** The resource server token services. */
    @Autowired
    @Qualifier("tokenServices")
    private ResourceServerTokenServices resourceServerTokenServices;
    
	/** The token store. */
	@Autowired
	private TokenStore tokenStore;
	
	/* (non-Javadoc)
	 * @see org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter#configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)
	 */
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
        .and().requestMatchers().antMatchers("/**")
        .and()
        .authorizeRequests()
		.antMatchers(HttpMethod.GET, "/restful/zoo/xmlzoo").access("#oauth2.hasScope('read')")//only for client with read scope
		.anyRequest().authenticated()//if this ".anyRequest().authenticated()" set before ".access("#oauth2.hasScope('read')")" we can authenticate by "Basic Auth", #oauth2.hasScope('read')- will ignore
//		antMatchers("/restful/oauth/token").access("hasRole('USER')")
        .and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler())
          .and()
          .formLogin().and()
          .httpBasic().and().logout().
           and()
          .csrf().disable();
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter#configure(org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer)
	 */
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.resourceId("restful").stateless(false);
		resources.authenticationManager(authenticationManager);
		resources.tokenStore(tokenStore);
		resources.tokenServices(resourceServerTokenServices);
	}

}
