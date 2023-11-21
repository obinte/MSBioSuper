/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.MSBiosuper.service;

import org.springframework.transaction.annotation.Transactional;

/**
 * @author mario
 */
@Transactional
public interface SecurityService {

    public String getToken(String user, String password, int timeExpiredDay) throws Exception;

}
