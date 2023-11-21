/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.MSBiosuper.controller;

import com.acosux.MSBiosuper.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author mario
 */
@RestController
@RequestMapping("/")
public class SecurityController {

    @Autowired
    SecurityService securityService;

    @RequestMapping(value = "/token/{user}/{password}/{timeExpiredDay}", method = {RequestMethod.GET})
    public ResponseEntity<String> getToken(
            @PathVariable("user") String user,
            @PathVariable("password") String password,
            @PathVariable("timeExpiredDay") int timeExpiredDay) {
        try {
            String token = securityService.getToken(user, password, timeExpiredDay);
            if (token != null) {
                return new ResponseEntity<>(token, HttpStatus.OK);
            }
        } catch (Exception e) {
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

}
