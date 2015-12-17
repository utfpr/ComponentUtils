/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.m4rc310.ui.gui.utils;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author marcelo
 */
@Retention (RetentionPolicy.RUNTIME) 
@Target(ElementType.FIELD)
@Documented
public @interface ToJSON {
    String key();
    String value() default "";
}
