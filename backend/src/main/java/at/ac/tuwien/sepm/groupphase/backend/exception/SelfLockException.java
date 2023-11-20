package at.ac.tuwien.sepm.groupphase.backend.exception;

public class SelfLockException extends RuntimeException {
    public SelfLockException(String message) {
        super(message);
    }
}
