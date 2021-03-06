package com.jcloud.auth.config.component;

import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.common.exceptions.RedirectMismatchException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.endpoint.RedirectResolver;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 授权码模式，客户端回调
 * @author jiaxm
 * @date 2021/3/30
 */
public class CustomRedirectResolver implements RedirectResolver {


    private Collection<String> redirectGrantTypes = Arrays.asList("implicit", "authorization_code");

    private boolean matchSubdomains = true;

    private boolean matchPorts = true;

    /**
     * 是否全地址匹配
     */
    private boolean patMatch = false;

    /**
     * Flag to indicate that requested URIs will match if they are a subdomain of the registered value.
     *
     * @param matchSubdomains the flag value to set (deafult true)
     */
    public void setMatchSubdomains(boolean matchSubdomains) {
        this.matchSubdomains = matchSubdomains;
    }

    /**
     * Flag that enables/disables port matching between the requested redirect URI and the registered redirect URI(s).
     *
     * @param matchPorts true to enable port matching, false to disable (defaults to true)
     */
    public void setMatchPorts(boolean matchPorts) {
        this.matchPorts = matchPorts;
    }

    /**
     * Grant types that are permitted to have a redirect uri.
     *
     * @param redirectGrantTypes the redirect grant types to set
     */
    public void setRedirectGrantTypes(Collection<String> redirectGrantTypes) {
        this.redirectGrantTypes = new HashSet<String>(redirectGrantTypes);
    }

    public String resolveRedirect(String requestedRedirect, ClientDetails client) throws OAuth2Exception {

        Set<String> authorizedGrantTypes = client.getAuthorizedGrantTypes();
        if (authorizedGrantTypes.isEmpty()) {
            throw new InvalidGrantException("A client must have at least one authorized grant type.");
        }
        if (!containsRedirectGrantType(authorizedGrantTypes)) {
            throw new InvalidGrantException(
                    "A redirect_uri can only be used by implicit or authorization_code grant types.");
        }

        Set<String> registeredRedirectUris = client.getRegisteredRedirectUri();
        if (registeredRedirectUris == null || registeredRedirectUris.isEmpty()) {
            throw new InvalidRequestException("At least one redirect_uri must be registered with the client.");
        }
        return obtainMatchingRedirect(registeredRedirectUris, requestedRedirect);
    }

    /**
     * @param grantTypes some grant types
     * @return true if the supplied grant types includes one or more of the redirect types
     */
    private boolean containsRedirectGrantType(Set<String> grantTypes) {
        for (String type : grantTypes) {
            if (redirectGrantTypes.contains(type)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Whether the requested redirect URI "matches" the specified redirect URI. For a URL, this implementation tests if
     * the user requested redirect starts with the registered redirect, so it would have the same host and root path if
     * it is an HTTP URL. The port is also matched.
     * <p>
     * For other (non-URL) cases, such as for some implicit clients, the redirect_uri must be an exact match.
     *
     * @param requestedRedirect The requested redirect URI.
     * @param redirectUri The registered redirect URI.
     * @return Whether the requested redirect URI "matches" the specified redirect URI.
     */
    protected boolean redirectMatches(String requestedRedirect, String redirectUri) {
        UriComponents requestedRedirectUri = UriComponentsBuilder.fromUriString(requestedRedirect).build();
        String requestedRedirectUriScheme = (requestedRedirectUri.getScheme() != null ? requestedRedirectUri.getScheme() : "");
        String requestedRedirectUriHost = (requestedRedirectUri.getHost() != null ? requestedRedirectUri.getHost() : "");
        String requestedRedirectUriPath = (requestedRedirectUri.getPath() != null ? requestedRedirectUri.getPath() : "");

        UriComponents registeredRedirectUri = UriComponentsBuilder.fromUriString(redirectUri).build();
        String registeredRedirectUriScheme = (registeredRedirectUri.getScheme() != null ? registeredRedirectUri.getScheme() : "");
        String registeredRedirectUriHost = (registeredRedirectUri.getHost() != null ? registeredRedirectUri.getHost() : "");
        String registeredRedirectUriPath = (registeredRedirectUri.getPath() != null ? registeredRedirectUri.getPath() : "");

        boolean portsMatch = this.matchPorts ? (registeredRedirectUri.getPort() == requestedRedirectUri.getPort()) : true;

        return registeredRedirectUriScheme.equals(requestedRedirectUriScheme) &&
                hostMatches(registeredRedirectUriHost, requestedRedirectUriHost) &&
                portsMatch &&
                // 全地址匹配
                (!patMatch || registeredRedirectUriPath.equals(StringUtils.cleanPath(requestedRedirectUriPath)));
    }

    /**
     * Check if host matches the registered value.
     *
     * @param registered the registered host
     * @param requested the requested host
     * @return true if they match
     */
    protected boolean hostMatches(String registered, String requested) {
        if (matchSubdomains) {
            return registered.equals(requested) || requested.endsWith("." + registered);
        }
        return registered.equals(requested);
    }

    /**
     * Attempt to match one of the registered URIs to the that of the requested one.
     *
     * @param redirectUris the set of the registered URIs to try and find a match. This cannot be null or empty.
     * @param requestedRedirect the URI used as part of the request
     * @return the matching URI
     * @throws RedirectMismatchException if no match was found
     */
    private String obtainMatchingRedirect(Set<String> redirectUris, String requestedRedirect) {
        Assert.notEmpty(redirectUris, "Redirect URIs cannot be empty");

        if (redirectUris.size() == 1 && requestedRedirect == null) {
            return redirectUris.iterator().next();
        }
        for (String redirectUri : redirectUris) {
            if (requestedRedirect != null && redirectMatches(requestedRedirect, redirectUri)) {
                return requestedRedirect;
            }
        }
        throw new RedirectMismatchException("Invalid redirect: " + requestedRedirect
                + " does not match one of the registered values.");
    }
}
