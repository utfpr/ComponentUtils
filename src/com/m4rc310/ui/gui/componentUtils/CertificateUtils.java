/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.m4rc310.ui.gui.componentUtils;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

/**
 *
 * @author marcelo
 */
public class CertificateUtils {
    private KeyStore ks;
    
    public X509Certificate getCertificate(String file, String senha){
        try {
            File _file = new File(file);
            
            if(!_file.exists()){
                throw new Exception("Arquivo de certificado ou senha inválida!");
            }
            
            ks = KeyStore.getInstance("PKCS12");
            ks.load(new FileInputStream(_file), senha.toCharArray());
            Enumeration<String> alias = ks.aliases();
            while (alias.hasMoreElements()) {
                String string = alias.nextElement();
                return (X509Certificate) ks.getCertificate(string);
            }
            
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        
        return null;
    }
    
    public static void main(String[] args) {
        X509Certificate cert = new CertificateUtils().getCertificate("5333495.pfx", "123456");
        System.out.println(cert.getIssuerDN());
        System.out.println(cert.getNotAfter());
        System.out.println(cert.getNotBefore());
    }
}
