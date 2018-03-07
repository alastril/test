package my.myname.oauth2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
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


/**
 * The Class AuthServerOAuth2Config.
 */
@Configuration
@EnableAuthorizationServer
@EnableWebSecurity
public class AuthServerOAuth2Config extends AuthorizationServerConfigurerAdapter {
	
	/** The Constant LOG. */
	private static final Logger LOG = Logger.getLogger(AuthServerOAuth2Config.class);
    
    /** The authentication manager for Oauth2 when checks tokens */
    @Autowired
    @Qualifier("oAuth2AuthenticationManager")
    private AuthenticationManager authenticationManager;
    
    /** The authentication manager for Oauth2 when "get" token */
    @Autowired
    @Qualifier("authenticationManagerUserPass")
    private AuthenticationManager authenticationManagerUserPass;
    
    /** The user details service for getting users from DB */
    @Autowired
	@Qualifier("userSecurityService")
	private UserDetailsService userDetailsService;
    
    /** The client details service. Save ClientDetails in memory for Oauth2 */
    @Autowired
    @Qualifier("inMemoryCDS")
    ClientDetailsService clientDetailsService;
    
    /** The authorization server token services. Load tokens from tokenStore using method "loadAuthentication", also can create and refresh token */
    @Autowired
    @Qualifier("tokenServices")
    private AuthorizationServerTokenServices authorizationServerTokenServices;
    
    /* (non-Javadoc)
     * @see org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter#configure(org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer)
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.tokenKeyAccess("permitAll()")
          .checkTokenAccess("isAuthenticated()");
        
    }

    /* (non-Javadoc)
     * @see org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter#configure(org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer)
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    	clients.withClientDetails(clientDetailsService);
//    	InMemoryClientDetailsServiceBuilder builder = clients.inMemory();
//    	builder.withClient("SampleClientId")
//          .secret("secret")
//          .authorizedGrantTypes("password", "authorization_code","client_credentials", "refresh_token", "implicit").resourceIds("restful")
//          .authorities("ROLE_ADMIN")
//          .scopes("read","write")
//          .autoApprove(true); 
//     Method method = builder.getClass().getDeclaredMethod("addClient", String.class, ClientDetails.class);
//     method.setAccessible(true);
//     method.invoke(builder,"pasha", clientDetailsService());
     System.out.println();
    }
 
    /* (non-Javadoc)
     * @see org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter#configure(org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer)
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenGranter(new ResourceOwnerPasswordTokenGranter(
        		authenticationManagerUserPass, authorizationServerTokenServices,
        		clientDetailsService,
        		new DefaultOAuth2RequestFactory(clientDetailsService)));  
    }
    
   /**
    * This object need for oauth2. He use in {@link TokenEndpoint}, postAccessToken method.
    *  In {@link InMemoryClientDetailsService} we store clients
    *  with credentials data
    *
    * @return the in memory client details service
    */
  @Bean("inMemoryCDS")
  public InMemoryClientDetailsService detailsService() {
	  
  	InMemoryClientDetailsService clientDetailsService = new InMemoryClientDetailsService();
  	Map<String, BaseClientDetails> clientDetailsStore = new HashMap<>();
  	BaseClientDetails baseClientDetails = new BaseClientDetails();
  	baseClientDetails.setClientId("pasha");
  	baseClientDetails.setClientSecret("baseSecret");
  	baseClientDetails.setScope(Arrays.asList("read","write"));
  	baseClientDetails.setAuthorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
  	baseClientDetails.setAuthorizedGrantTypes(Arrays.asList("authorization_code",
  			"password", "client_credentials", "implicit", "refresh_token"));
  	baseClientDetails.setAccessTokenValiditySeconds(3600);
  	clientDetailsStore.put("pasha", baseClientDetails);
  	
  	baseClientDetails = new BaseClientDetails();
  	baseClientDetails.setClientId("SampleClientId");
  	baseClientDetails.setClientSecret("secret");
  	baseClientDetails.setScope(Arrays.asList("read","write"));
  	baseClientDetails.setAuthorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
  	baseClientDetails.setAuthorizedGrantTypes(Arrays.asList("authorization_code",
  			"password", "client_credentials", "implicit", "refresh_token"));
  	baseClientDetails.setAccessTokenValiditySeconds(3600);
  	clientDetailsStore.put("SampleClientId", baseClientDetails);
  	
  	clientDetailsService.setClientDetailsStore(clientDetailsStore);
  	
  	LOG.info("InMemoryClientDetailsService is created.");
  	return clientDetailsService;
  }
    
    /**
     * Authorization server token services. Load tokens from tokenStore using method "loadAuthentication", also can create and refresh token
     *
     * @param clientDetailsService the client details service
     * @param tokenStore the token store
     * @return the authorization server token services
     */
    @Bean("tokenServices")
    @Autowired
    public AuthorizationServerTokenServices authorizationServerTokenServices(ClientDetailsService clientDetailsService, TokenStore  tokenStore) {
    	DefaultTokenServices defaultTokenServices  = new DefaultTokenServices();
    	defaultTokenServices.setClientDetailsService(clientDetailsService);
    	defaultTokenServices.setTokenStore(tokenStore);
    	defaultTokenServices.setAccessTokenValiditySeconds(3600);
    	defaultTokenServices.setAuthenticationManager(authenticationManager);
    	LOG.info("AuthorizationServerTokenServices is created.");
    	return defaultTokenServices;
    }
    
    /**
     * This object, {@link OAuth2AuthenticationManager}, use to give grant to resources by token value
     *
     * @param clientDetailsService the client details service
     * @param authorizationServerTokenServices the authorization server token services
     * @return the o auth2 authentication manager
     */
    @Bean("oAuth2AuthenticationManager")
    @Autowired
    @DependsOn("tokenServices")
    public OAuth2AuthenticationManager authenticationManager(ClientDetailsService clientDetailsService, ResourceServerTokenServices authorizationServerTokenServices) {
    	OAuth2AuthenticationManager authenticationManager = new OAuth2AuthenticationManager();
    	authenticationManager.setClientDetailsService(clientDetailsService);
    	authenticationManager.setTokenServices(authorizationServerTokenServices);
    	authenticationManager.setResourceId("restful");
    	LOG.info("OAuth2AuthenticationManager is created.");
    	return authenticationManager;
    }
    
    /**
     * Gets the provider manager. Provider manager use for authenticate users and give them a token in success case.
     *
     * @param providerCustom the provider custom
     * @return the provider manager
     */
    @Bean("authenticationManagerUserPass")
    @Autowired
    public ProviderManager getProviderManager(ProviderClientCustom providerCustom) {
    	List<AuthenticationProvider> authenticationProviders = new ArrayList<>();
    	authenticationProviders.add(providerCustom);
    	ProviderManager providerManager = new ProviderManager(authenticationProviders);
    	LOG.info("ProviderManager is created.");
		return providerManager;
    }
    
    /**
     * Token store.
     *
     * @return the token store
     */
    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }
}
