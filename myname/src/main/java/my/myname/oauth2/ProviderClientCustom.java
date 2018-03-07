package my.myname.oauth2;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.stereotype.Component;


/**
 * use for getting token
 * The Class ProviderClientCustom.
 */
@Component
public class ProviderClientCustom extends AbstractUserDetailsAuthenticationProvider{
	
	/** The Constant LOG. */
	private static final Logger LOG = Logger.getLogger(ProviderClientCustom.class);
	
	/** The client details service. */
	@Autowired
	@Qualifier("inMemoryCDS")
	private ClientDetailsService clientDetailsService;
    
    /** The user details service. */
    @Autowired
	@Qualifier("userSecurityService")
	private UserDetailsService userDetailsService;
	
	/* (non-Javadoc)
	 * @see org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider#additionalAuthenticationChecks(org.springframework.security.core.userdetails.UserDetails, org.springframework.security.authentication.UsernamePasswordAuthenticationToken)
	 */
    /**
     * if user exists in DB than checks secretCode of Client.
     * Important!!!! In oauth2 lib users always check by existing in clientDetailsService. For example:
     * if we try get token by "userName", so in {@link TokenEndpoint} we try to get client by id "userName", not UserDetails by using DB.
     * but we must be authenticated in system, default is by Basic Auth(here in our case is UserDetails with DB).
     * In General: "Petya" try auth by basic if true then try find "Petya" in clientDetailsService and then try "give" us token.
     */
	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		ClientDetails clientDetails= clientDetailsService.loadClientByClientId(userDetails.getUsername());
		if(clientDetails == null) throw new BadCredentialsException("No such clientId for Oauth2: "+ userDetails.getUsername());
		if(clientDetails != null && authentication!=null &&
				!clientDetails.getClientSecret().equals(authentication.getCredentials())) {
			throw new BadCredentialsException("Wrong client secret for user: "+ authentication.getCredentials().toString());
		}
		LOG.debug(authentication);
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider#retrieveUser(java.lang.String, org.springframework.security.authentication.UsernamePasswordAuthenticationToken)
	 */
	/**
	 * getting user from DB
	 */
	@Override
	protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {

		UserDetails detailsBuilder = userDetailsService.loadUserByUsername(username);
		if(detailsBuilder == null) throw new BadCredentialsException("No such user: "+ username);
		LOG.debug("Find user: "+detailsBuilder);
		return detailsBuilder;
	}


}
