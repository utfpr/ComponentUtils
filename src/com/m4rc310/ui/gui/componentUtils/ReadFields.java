/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.m4rc310.ui.gui.componentUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author mls
 */
@Target({ElementType.FIELD, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ReadFields {

    public enum TypeAlignment {

        RIGTH, CENTER, LEFT
    }

    String field();

    String format() default "";

    boolean enable() default true;

    boolean editable() default true;

    boolean visible() default true;

    TypeAlignment alignment() default TypeAlignment.RIGTH;
}
