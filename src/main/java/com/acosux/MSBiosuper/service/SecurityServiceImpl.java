/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.MSBiosuper.service;

import com.acosux.MSBiosuper.security.TokenUtil;
import com.acosux.MSBiosuper.util.Encriptacion;
import org.springframework.stereotype.Service;

/**
 * @author mario
 */
@Service
public class SecurityServiceImpl implements SecurityService {

    final String usrVitapro = "adminbio";
    final String passVitapro = "8601e29778f60cb8f547246e2aa3694cb0cc597298645ae26e2725a2fc3dad00";

    @Override
    public String getToken(String user, String password, int timeExpiredDay) throws Exception {
        Encriptacion encriptacion = new Encriptacion();
        TokenUtil tokenUtil = new TokenUtil();
        String clave = encriptacion.sha256(password);
        if (user.equals(usrVitapro) && clave.equals(passVitapro)) {
            return "Bearer " + tokenUtil.createTokenForUser(user, timeExpiredDay);
        }
        return "";
    }

}
