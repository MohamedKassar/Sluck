package fr.upmc.sluck.utils.exceptions;

/**
 * Created by ktare on 18/03/2018.
 */

public class UtilException extends Exception{
    public enum ExceptionType{FILE_CREATION, FOLDER_CREATION, FILE_READ, FILE_WRITE, SERVER_ALREADY_CREATED}
    private String message;
    public UtilException(ExceptionType type, String name) {
        switch (type){
            case FILE_READ:
                message = "Unable to read file: " + name;
                break;
            case FILE_WRITE:
                message = "Unable to write in file: " + name;
                break;
            case FILE_CREATION:
                message = "Unable to create file: " + name;
                break;
            case FOLDER_CREATION:
                message = "Unable to create folder: " + name;
                break;
            case SERVER_ALREADY_CREATED:
                message = "Server is already created";
                break;
        }

    }

    @Override
    public String getMessage() {
        return message;
    }
}
