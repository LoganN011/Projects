package smart.rails;

/**
 * Type of message that a thread can pass to another thread
 * used to make handle message work better
 *@author Cameron Fox and Logan Nunno
 */
public enum MessageType {
    PRINT_CONNECTIONS,
    SEEK_PATH,
    PATH_FOUND,
    NO_PATH,
    LOCK_PATH,
    LOCK_FAILED,
    LOCK_SUCCESS,
    MOVE_TRAIN
}
