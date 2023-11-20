package at.ac.tuwien.sepm.groupphase.backend.exception;

//Is thrown when a locked User tries to log in
public class UserLockedException extends RuntimeException {
    public UserLockedException(String message) {
        super(message);
    }
}
