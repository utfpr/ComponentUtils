/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.tsi.utils;

/**
 *
 * @author Marcelo
 */
public class LogAdapter implements LogListener {

    @Override
    public void errorMessage(Object source, String message) {
    }

    @Override
    public void infoMessage(Object source, String message) {
    }

    @Override
    public void warningMessage(Object source, String message) {
    }

    @Override
    public void debugMessage(Object source, String message) {
    }

    @Override
    public void debugMessageDetails(LogServer.EnumLogType type, Object source, String message) {
    }

}
