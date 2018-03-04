package my.myname.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;


@Configuration
@EnableResourceServer
public class ResourceServerConfiguration  extends ResourceServerConfigurerAdapter{
    @Autowired
    @Qualifier("oAuth2AuthenticationManager")
    private AuthenticationManager authenticationManager;
//    @Autowired
//	@Qualifier("userSecurityService")
//	private UserDetailsService userDetailsService;
    @Autowired
    @Qualifier("tokenServices")
    private ResourceServerTokenServices resourceServerTokenServices;
    
	@Autowired
	private TokenStore tokenStore;
	@Override
	public void configure(HttpSecurity http) throws Exception {

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
        and().requestMatchers().antMatchers("/**")
        .and()
        .authorizeRequests().
		antMatchers(HttpMethod.GET, "/restful/zoo/xmlzoo").access("#oauth2.hasScope('read')")
//		antMatchers("/restful/oauth/token").access("hasRole('USER')")
        .and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler())
          .and()
          .formLogin().and()
          .httpBasic().and().logout().
           and()
          .csrf().disable();
	}
	
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.resourceId("restful").stateless(false);
		resources.authenticationManager(authenticationManager);
		resources.tokenStore(tokenStore);
		resources.tokenServices(resourceServerTokenServices);
		
	}

}
