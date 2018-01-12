package com.stumpner.mediadesk.web.webdav.auth;

/*********************************************************
 Copyright 2017 by Franz STUMPNER (franz@stumpner.com)

 openMEDIADESK is licensed under Apache License Version 2.0

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 *********************************************************/

/**
 * Created by IntelliJ IDEA.
 * User: franz.stumpner
 * Date: 05.05.2011
 * Time: 11:11:27
 * To change this template use File | Settings | File Templates.
 */
public class WebdavSpringAuthFilter {

    /*

    //~ Static fields/initializers =====================================================================================


    //private static final Log logger = LogFactory.getLog(DigestAuthenticationFilter.class);

    //~ Instance fields ================================================================================================

    //private AuthenticationDetailsSource authenticationDetailsSource = new WebAuthenticationDetailsSource();
    //private DigestAuthenticationEntryPoint authenticationEntryPoint;
    //protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
    //private UserCache userCache = new NullUserCache();
    //private UserDetailsService userDetailsService;
    private boolean passwordAlreadyEncoded = false;
    private boolean createAuthenticatedToken = false;

    //~ Methods ========================================================================================================



    @Override
    public void afterPropertiesSet() {
        //Assert.notNull(userDetailsService, "A UserDetailsService is required");
        //Assert.notNull(authenticationEntryPoint, "A DigestAuthenticationEntryPoint is required");
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String header = request.getHeader("Authorization");

        //if (logger.isDebugEnabled()) {
            System.out.println("Authorization header received from user agent: " + header);
        //}

        if ((header != null) && header.startsWith("Digest ")) {
            String section212response = header.substring(7);

            String[] headerEntries = DigestAuthUtils.splitIgnoringQuotes(section212response, ',');
            Map<String,String> headerMap = DigestAuthUtils.splitEachArrayElementAndCreateMap(headerEntries, "=", "\"");

            String username = headerMap.get("username");
            String realm = headerMap.get("realm");
            String nonce = headerMap.get("nonce");
            String uri = headerMap.get("uri");
            String responseDigest = headerMap.get("response");
            String qop = headerMap.get("qop"); // RFC 2617 extension
            String nc = headerMap.get("nc"); // RFC 2617 extension
            String cnonce = headerMap.get("cnonce"); // RFC 2617 extension

            // Check all required parameters were supplied (ie RFC 2069)
            if ((username == null) || (realm == null) || (nonce == null) || (uri == null) || (response == null)) {
                //if (logger.isDebugEnabled()) {
                    System.out.println("extracted username: '" + username + "'; realm: '" + username + "'; nonce: '"
                            + username + "'; uri: '" + username + "'; response: '" + username + "'");
                //}

                fail(request, response,
                        new BadCredentialsException(messages.getMessage("DigestAuthenticationFilter.missingMandatory",
                                new Object[]{section212response}, "Missing mandatory digest value; received header {0}")));

                return;
            }

            // Check all required parameters for an "auth" qop were supplied (ie RFC 2617)
            if ("auth".equals(qop)) {
                if ((nc == null) || (cnonce == null)) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("extracted nc: '" + nc + "'; cnonce: '" + cnonce + "'");
                    }

                    fail(request, response,
                            new BadCredentialsException(messages.getMessage("DigestAuthenticationFilter.missingAuth",
                                    new Object[]{section212response}, "Missing mandatory digest value; received header {0}")));

                    return;
                }
            }

            // Check realm name equals what we expected
            if (!this.getAuthenticationEntryPoint().getRealmName().equals(realm)) {
                fail(request, response,
                        new BadCredentialsException(messages.getMessage("DigestAuthenticationFilter.incorrectRealm",
                                new Object[]{realm, this.getAuthenticationEntryPoint().getRealmName()},
                                "Response realm name '{0}' does not match system realm name of '{1}'")));

                return;
            }

            // Check nonce was a Base64 encoded (as sent by DigestAuthenticationEntryPoint)
            if (!Base64.isBase64(nonce.getBytes())) {
                fail(request, response,
                        new BadCredentialsException(messages.getMessage("DigestAuthenticationFilter.nonceEncoding",
                                new Object[]{nonce}, "Nonce is not encoded in Base64; received nonce {0}")));

                return;
            }

            // Decode nonce from Base64
            // format of nonce is:
            //   base64(expirationTime + ":" + md5Hex(expirationTime + ":" + key))
            String nonceAsPlainText = new String(Base64.decode(nonce.getBytes()));
            String[] nonceTokens = StringUtils.delimitedListToStringArray(nonceAsPlainText, ":");

            if (nonceTokens.length != 2) {
                fail(request, response,
                        new BadCredentialsException(messages.getMessage("DigestAuthenticationFilter.nonceNotTwoTokens",
                                new Object[]{nonceAsPlainText}, "Nonce should have yielded two tokens but was {0}")));

                return;
            }

            // Extract expiry time from nonce
            long nonceExpiryTime;

            try {
                nonceExpiryTime = new Long(nonceTokens[0]).longValue();
            } catch (NumberFormatException nfe) {
                fail(request, response,
                        new BadCredentialsException(messages.getMessage("DigestAuthenticationFilter.nonceNotNumeric",
                                new Object[]{nonceAsPlainText},
                                "Nonce token should have yielded a numeric first token, but was {0}")));

                return;
            }

            // Check signature of nonce matches this expiry time
            String expectedNonceSignature = DigestAuthUtils.md5Hex(nonceExpiryTime + ":"
                    + this.getAuthenticationEntryPoint().getKey());

            if (!expectedNonceSignature.equals(nonceTokens[1])) {
                fail(request, response,
                        new BadCredentialsException(messages.getMessage("DigestAuthenticationFilter.nonceCompromised",
                                new Object[]{nonceAsPlainText}, "Nonce token compromised {0}")));

                return;
            }

            // Lookup password for presented username
            // NB: DAO-provided password MUST be clear text - not encoded/salted
            // (unless this instance's passwordAlreadyEncoded property is 'false')
            boolean loadedFromDao = false;
            UserDetails user = userCache.getUserFromCache(username);

            if (user == null) {
                loadedFromDao = true;

                try {
                    user = userDetailsService.loadUserByUsername(username);
                } catch (UsernameNotFoundException notFound) {
                    fail(request, response,
                            new BadCredentialsException(messages.getMessage("DigestAuthenticationFilter.usernameNotFound",
                                    new Object[]{username}, "Username {0} not found")));

                    return;
                }

                if (user == null) {
                    throw new AuthenticationServiceException(
                            "AuthenticationDao returned null, which is an interface contract violation");
                }

                userCache.putUserInCache(user);
            }

            // Compute the expected response-digest (will be in hex form)
            String serverDigestMd5;

            // Don't catch IllegalArgumentException (already checked validity)
            serverDigestMd5 = DigestAuthUtils.generateDigest(passwordAlreadyEncoded, username, realm, user.getPassword(),
                    request.getMethod(), uri, qop, nonce, nc, cnonce);

            // If digest is incorrect, try refreshing from backend and recomputing
            if (!serverDigestMd5.equals(responseDigest) && !loadedFromDao) {
                if (logger.isDebugEnabled()) {
                    logger.debug(
                            "Digest comparison failure; trying to refresh user from DAO in case password had changed");
                }

                try {
                    user = userDetailsService.loadUserByUsername(username);
                } catch (UsernameNotFoundException notFound) {
                    // Would very rarely happen, as user existed earlier
                    fail(request, response,
                            new BadCredentialsException(messages.getMessage("DigestAuthenticationFilter.usernameNotFound",
                                    new Object[]{username}, "Username {0} not found")));
                }

                userCache.putUserInCache(user);

                // Don't catch IllegalArgumentException (already checked validity)
                serverDigestMd5 = DigestAuthUtils.generateDigest(passwordAlreadyEncoded, username, realm, user.getPassword(),
                        request.getMethod(), uri, qop, nonce, nc, cnonce);
            }

            // If digest is still incorrect, definitely reject authentication attempt
            if (!serverDigestMd5.equals(responseDigest)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Expected response: '" + serverDigestMd5 + "' but received: '" + responseDigest
                            + "'; is AuthenticationDao returning clear text passwords?");
                }

                fail(request, response,
                        new BadCredentialsException(messages.getMessage("DigestAuthenticationFilter.incorrectResponse",
                                "Incorrect response")));
                return;
            }

            // To get this far, the digest must have been valid
            // Check the nonce has not expired
            // We do this last so we can direct the user agent its nonce is stale
            // but the request was otherwise appearing to be valid
            if (nonceExpiryTime < System.currentTimeMillis()) {
                fail(request, response,
                        new NonceExpiredException(messages.getMessage("DigestAuthenticationFilter.nonceExpired",
                                "Nonce has expired/timed out")));

                return;
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Authentication success for user: '" + username + "' with response: '" + responseDigest
                        + "'");
            }

            UsernamePasswordAuthenticationToken authRequest;
            if (createAuthenticatedToken) {
                   authRequest = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
            }
            else
            {
                authRequest = new UsernamePasswordAuthenticationToken(user, user.getPassword());
            }

            authRequest.setDetails(authenticationDetailsSource.buildDetails((HttpServletRequest) request));

            SecurityContextHolder.getContext().setAuthentication(authRequest);
        }

        chain.doFilter(request, response);
    }

    /*
    private void fail(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(null);

        if (logger.isDebugEnabled()) {
            logger.debug(failed);
        }

        authenticationEntryPoint.commence(request, response, failed);
    } */

    /*

    public DigestAuthenticationEntryPoint getAuthenticationEntryPoint() {
        return authenticationEntryPoint;
    }

    public UserCache getUserCache() {
        return userCache;
    }

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setAuthenticationDetailsSource(AuthenticationDetailsSource authenticationDetailsSource) {
        Assert.notNull(authenticationDetailsSource, "AuthenticationDetailsSource required");
        this.authenticationDetailsSource = authenticationDetailsSource;
    }

    public void setAuthenticationEntryPoint(DigestAuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }

    public void setPasswordAlreadyEncoded(boolean passwordAlreadyEncoded) {
        this.passwordAlreadyEncoded = passwordAlreadyEncoded;
    }

    public void setUserCache(UserCache userCache) {
        this.userCache = userCache;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


    /**
     * If you set this property, the Authentication object, which is
     * created after the successful digest authentication will be marked
     * as <b>authenticated</b> and filled with the authorities loaded by
     * the UserDetailsService. It therefore will not be re-authenticated
     * by your AuthenticationProvider. This means, that only the password
     * of the user is checked, but not the flags like isEnabled() or
     * isAccountNonExpired(). You will save some time by enabling this flag,
     * as otherwise your UserDetailsService will be called twice. A more secure
     * option would be to introduce a cache around your UserDetailsService, but
     * if you don't use these flags, you can also safely enable this option.
     *
     * @param createAuthenticatedToken default is false
     */
    /*
    public void setCreateAuthenticatedToken(boolean createAuthenticatedToken) {
        this.createAuthenticatedToken = createAuthenticatedToken;
    } */


}
