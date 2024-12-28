package src.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class CircularQueue<E> implements Queue<E> {
    private final E[] queue;
    private final int capacity;
    private int size;
    private int head;
    private int tail;

    @SuppressWarnings("unchecked")
    public CircularQueue(int capacity) {
        this.capacity = capacity;
        this.queue = (E[]) new Object[capacity];
        this.size = 0;
        this.head = 0;
        this.tail = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int free() {
        return capacity - size;
    }

    @Override
    public boolean empty() {
        return size == 0;
    }

    @Override
    public boolean full() {
        return size == capacity;
    }

    @Override
    public E peekFirst() {
        if (empty()) {
            throw new NoSuchElementException("Queue is empty, no first element.");
        }
        return queue[head];
    }

    @Override
    public E get() {
        if (empty()) {
            throw new NoSuchElementException("Queue is empty, no elements to get.");
        }
        E result = queue[head];
        head = (head + 1) % capacity;
        size--;
        return result;
    }

    @Override
    public void put(E element) {
        if (full()) {
            throw new IllegalStateException("Queue is full, can't add elements.");
        }
        queue[tail] = element;
        tail = (tail + 1) % capacity;
        size++;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        if (size > 0) {
            int index = head;
            sb.append(queue[index]);
            for (int i = 1; i < size; i++) {
                index = (head + i) % capacity;
                sb.append(", ").append(queue[index]);
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public Iterator<E> iterator() {
        return new CircularQueueIterator();
    }

    private class CircularQueueIterator implements Iterator<E> {
        private int cursor;
        private int remaining;
        private boolean isMovable;

        public CircularQueueIterator() {
            this.cursor = head;
            this.remaining = size;
            this.isMovable = false;
        }

        @Override
        public boolean hasNext() {
            return remaining > 0;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            E result = queue[cursor];
            cursor = (cursor + 1) % capacity;
            remaining--;
            isMovable = true;
            return result;
        }

        @Override
        public void remove() {
            if (!isMovable) {
                throw new IllegalStateException();
            }
            int removeIndex = (cursor - 1 + capacity) % capacity;
            for (int i = removeIndex; i != tail; i = (i + 1) % capacity) {
                int nextIndex = (i + 1) % capacity;
                queue[i] = queue[nextIndex];
            }
            tail = (tail - 1 + capacity) % capacity;
            size--;
            isMovable = false;
        }
    }
}