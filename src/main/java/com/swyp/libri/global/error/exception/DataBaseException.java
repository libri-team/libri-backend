package com.swyp.libri.global.error.exception;

public class DataBaseException extends RuntimeException {
    private static final long serialVersionUID = 3L;

    private ErrorCode errorCode;
    private String[] stringArgList;

    public DataBaseException(Exception e){
        super(e);
        this.errorCode = ErrorCode.ERR_DB_INSERT;
        this.stringArgList = new String[]{};
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
