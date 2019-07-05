
package com.emprovise.service.security.config.okta;

import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.token.store.JwtClaimsSetVerifier;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;

@Component
public class CustomClaimVerifier implements JwtClaimsSetVerifier {

    private static final String SUBJECT_CLAIM = "sub";
    private static final String AUDIENCE_CLAIM = "aud";
    private static final String SCOPE_CLAIM = "scp";
    private static final String OPEN_ID_SCOPE = "openid";
    private static final Map<String, String> clientIdUser = new HashMap<>();
    private static final List<String> audClaimList = Arrays.asList("default", "emprovise-api");

    static {
        clientIdUser.put("56466k64kl64n6n", "J001234");
        clientIdUser.put("456kl456kln65n6", "A983245");
    }

    @Override
    public void verify(Map<String, Object> claims) throws InvalidTokenException {

        List<String> scopes = Optional.ofNullable(claims.get(SCOPE_CLAIM)).map(list -> (List<String>)list)
                .orElseGet(Collections::emptyList);

        String username = Optional.ofNullable(claims.get(SUBJECT_CLAIM)).map(Object::toString).orElse("");

        if(StringUtils.isEmpty(username) && clientIdUser.containsKey(claims.get("cid"))) {
            username = clientIdUser.get(claims.get("cid"));
        }

        if (scopes.contains(OPEN_ID_SCOPE) && StringUtils.isEmpty(username)) {
            throw new InvalidTokenException(SUBJECT_CLAIM);
        }

        String aud = Optional.ofNullable(claims.get(AUDIENCE_CLAIM)).map(Object::toString).orElse("");

        if (!audClaimList.contains(aud)) {
            throw new InvalidTokenException(AUDIENCE_CLAIM);
        }
    }
}
