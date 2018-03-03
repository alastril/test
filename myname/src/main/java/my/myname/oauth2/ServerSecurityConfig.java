package my.myname.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
@EnableWebSecurity
///oauth/token
public class ServerSecurityConfig extends WebSecurityConfigurerAdapter  {
	
	@Autowired
    private ClientDetailsService clientDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
 
    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.requestMatchers()
//          .antMatchers("/restful/zoo/xmlzoo", "/oauth/authorize").antMatchers("/**")
//          .and()
//          .authorizeRequests()
//          .anyRequest().authenticated()
//          .and()
//          .formLogin().and()
//          .httpBasic().and().logout().
//           and()
//          .csrf().disable();
        
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
        and().requestMatchers().antMatchers("/**")
        .and()
        .authorizeRequests()
          .antMatchers(HttpMethod.POST,"/restful/zoo/xmlzoo").authenticated()
          .antMatchers("/oauth/authorize").permitAll()
          .antMatchers("/oauth/token").permitAll()
          .and()
          .formLogin().and()
          .httpBasic().and().logout().
           and()
          .csrf().disable();
    }
 
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.parentAuthenticationManager(authenticationManager)
          .inMemoryAuthentication()
          .withUser("john").password("123").roles("USER");
    }
//    
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//        .csrf().disable()
//        .anonymous().disable()
//        .authorizeRequests()
//        .antMatchers("/oauth/token").permitAll();
//    }
 
//    @Override
//    @Bean
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
 
 
    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }
 
    @Bean
    @Autowired
    public TokenStoreUserApprovalHandler userApprovalHandler(TokenStore tokenStore){
        TokenStoreUserApprovalHandler handler = new TokenStoreUserApprovalHandler();
        handler.setTokenStore(tokenStore);
        handler.setRequestFactory(new DefaultOAuth2RequestFactory(clientDetailsService));
        handler.setClientDetailsService(clientDetailsService);
        return handler;
    }
     
    @Bean
    @Autowired
    public ApprovalStore approvalStore(TokenStore tokenStore) throws Exception {
        TokenApprovalStore store = new TokenApprovalStore();
        store.setTokenStore(tokenStore);
        return store;
    }
}
