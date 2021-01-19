package neumont.donavon.tommy.LabOne.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    private long id;

    private String stringID;

    public ResourceNotFoundException(long id) {
        this.id = id;
    }

    public ResourceNotFoundException() {

    }

    public long getId() {
        return id;
    }

}
