/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.tsi.utils;

import br.edu.utfpr.cm.tsi.utils.LogServer.EnumLogType;

/**
 *
 * @author Marcelo
 */
public interface LogListener {

    void errorMessage(Object source, String message);

    void infoMessage(Object source, String message);

    void warningMessage(Object source, String message);

    void debugMessage(Object source, String message);

    void debugMessageDetails(EnumLogType type, Object source, String message);
}
