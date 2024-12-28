package src.util;

/**
 * Interface representing a queue data structure.
 *
 * @param <E> the type of elements in this queue
 */
public interface Queue<E> extends Iterable<E> {

    /**
     * Returns the number of elements in this queue.
     *
     * @return the number of elements in this queue
     */
    int size();

    /**
     * Returns the space currently available in this queue.
     *
     * @return the space currently available in this queue
     */
    int free();

    /**
     * Returns true if this queue contains no elements.
     * Equivalent to {@code size() == 0}.
     *
     * @return true if this queue is empty, false otherwise
     */
    boolean empty();

    /**
     * Returns true if no space is currently available in this queue.
     * Equivalent to {@code !hasFree(1)}.
     *
     * @return true if this queue is full, false otherwise
     */
    boolean full();

    /**
     * Retrieves, but does not remove, the head (first element) of this queue,
     * or returns null if this queue is empty.
     *
     * @return the head of this queue, or null if this queue is empty
     */
    E peekFirst();

    /**
     * Retrieves and removes the head of this queue.
     *
     * @return the head of this queue
     * @throws IllegalStateException if this queue is empty
     */
    E get() throws IllegalStateException;

    /**
     * Inserts the specified element at the tail of this queue.
     *
     * @param e the element to add
     * @throws IllegalStateException if this queue is full
     */
    void put(E e) throws IllegalStateException;
}