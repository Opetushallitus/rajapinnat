/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.vm.sade.rajapinnat.ytj.api.exception;

/**
 *
 * @author Tuomas Katva
 */
public class YtjConnectionException extends Exception {
    
    private String exceptionCode;
    private String message;
    
    public YtjConnectionException(String expCode,String msg) {
        this.exceptionCode = expCode;
        this.message = msg;
    }
    
    public String getExceptionCode() {
        return exceptionCode;
    }

    @Override
    public String toString() {
        return exceptionCode + " " + message;
    }
    
    
    @Override
    public String getMessage() {
        return message;
    }
}
