package neumont.donavon.tommy.LabOne.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    private final long id;

    public ResourceNotFoundException(long id)
    {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
