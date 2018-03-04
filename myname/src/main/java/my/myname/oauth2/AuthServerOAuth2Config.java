package my.myname.oauth2;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.InMemoryClientDetailsService;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
@EnableAuthorizationServer
@EnableWebSecurity
public class AuthServerOAuth2Config extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private TokenStore tokenStore;
    @Autowired
    @Qualifier("oAuth2AuthenticationManager")
    private AuthenticationManager authenticationManager;
    @Autowired
    private AuthenticationManager authenticationManagerUserPass;
    @Autowired
	@Qualifier("userSecurityService")
	private UserDetailsService userDetailsService;
    @Autowired
    ClientDetailsService clientDetailsService;
    @Autowired
    @Qualifier("tokenServices")
    private AuthorizationServerTokenServices authorizationServerTokenServices;
    
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.tokenKeyAccess("permitAll()")
          .checkTokenAccess("isAuthenticated()");
        
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    	InMemoryClientDetailsServiceBuilder builder = clients.inMemory();
    	builder.withClient("SampleClientId")
          .secret("secret")
          .authorizedGrantTypes("password", "authorization_code","client_credentials", "refresh_token", "implicit").resourceIds("restful")
          .authorities("ROLE_ADMIN")
          .scopes("read","write")
          .autoApprove(true); 
     Method method = builder.getClass().getDeclaredMethod("addClient", String.class, ClientDetails.class);
     method.setAccessible(true);
     method.invoke(builder,"pasha", clientDetailsService());
     System.out.println();
    }
 
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        endpoints.tokenStore(tokenStore)
//                .authenticationManager(authenticationManager);
//        endpoints.userDetailsService(userDetailsService);
        //TODO authentication for clientDetailServices
        endpoints.tokenGranter(new ResourceOwnerPasswordTokenGranter(
        		authenticationManagerUserPass, authorizationServerTokenServices,
        		clientDetailsService,
        		new DefaultOAuth2RequestFactory(clientDetailsService)));
        endpoints.setClientDetailsService(clientDetailsService);
//        endpoints.tokenServices(authorizationServerTokenServices);
//        endpoints.allowedTokenEndpointRequestMethods(HttpMethod.POST, HttpMethod.GET);
      
    }

    
    /**
     * Test client user
     * @return
     */
    public BaseClientDetails clientDetailsService() {
    	BaseClientDetails baseClientDetails = new BaseClientDetails();
    	baseClientDetails.setClientId("pasha");
    	baseClientDetails.setClientSecret("baseSecret");
    	baseClientDetails.setScope(Arrays.asList("read","write"));
    	baseClientDetails.setAuthorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
    	baseClientDetails.setAuthorizedGrantTypes(Arrays.asList("authorization_code",
    			"password", "client_credentials", "implicit", "refresh_token"));
    	baseClientDetails.setAccessTokenValiditySeconds(3600);
    	return baseClientDetails;
    }
//  @Bean("inMemoryCDS")
//  public InMemoryClientDetailsService detailsService() {
//
//  	InMemoryClientDetailsService clientDetailsService = new InMemoryClientDetailsService();
//  	Map<String, BaseClientDetails> clientDetailsStore = new HashMap<>();
//  	BaseClientDetails baseClientDetails = new BaseClientDetails();
//  	baseClientDetails.setClientId("pasha");
//  	baseClientDetails.setClientSecret("baseSecret");
//  	baseClientDetails.setScope(Arrays.asList("read","write"));
//  	baseClientDetails.setAuthorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
//  	baseClientDetails.setAuthorizedGrantTypes(Arrays.asList("authorization_code",
//  			"password", "client_credentials", "implicit", "refresh_token"));
//  	baseClientDetails.setAccessTokenValiditySeconds(3600);
//  	clientDetailsStore.put("pasha", baseClientDetails);
//  	clientDetailsService.setClientDetailsStore(clientDetailsStore);
//  	
//  	
//  	return clientDetailsService;
//  }
    
    @Bean("tokenServices")
    @Autowired
    public AuthorizationServerTokenServices authorizationServerTokenServices(TokenStore  tokenStore) {
    	DefaultTokenServices defaultTokenServices  = new DefaultTokenServices();
    	defaultTokenServices.setClientDetailsService(clientDetailsService);
    	defaultTokenServices.setTokenStore(tokenStore);
    	defaultTokenServices.setAccessTokenValiditySeconds(3600);
    	defaultTokenServices.setAuthenticationManager(authenticationManager);
    	return defaultTokenServices;
    }
    
    @Bean("oAuth2AuthenticationManager")
    @Autowired
    @DependsOn("tokenServices")
    public OAuth2AuthenticationManager authenticationManager(ResourceServerTokenServices authorizationServerTokenServices) {
    	OAuth2AuthenticationManager authenticationManager = new OAuth2AuthenticationManager();
    	authenticationManager.setClientDetailsService(clientDetailsService);
    	authenticationManager.setTokenServices(authorizationServerTokenServices);
    	authenticationManager.setResourceId("restful");
    
    	return authenticationManager;
    }
    
    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }
}
