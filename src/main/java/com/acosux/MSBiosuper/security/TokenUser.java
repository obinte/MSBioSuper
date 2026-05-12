package com.acosux.MSBioSuper.security;

import org.springframework.security.core.authority.AuthorityUtils;

public class TokenUser extends org.springframework.security.core.userdetails.User {

    public TokenUser(String user, String clave) {
        super(user, clave, AuthorityUtils.createAuthorityList("ADMIN"));
    }

}
