package customqueueV2;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;

import static java.util.Objects.requireNonNull;

/**
 * A singly linked list implementation of the {@link Queue} interface.
 *
 * <p>Elements are stored in a chain of internal {@link Node} objects.
 * {@code head} references the first node (the front of the queue, and the
 * next element to be returned by {@link #poll()}/{@link #remove()}), and
 * {@code tail} references the last node (the back of the queue, where
 * {@link #add(Object)}/{@link #offer(Object)} insert new elements). Unlike
 * an array-backed queue, this implementation never needs to resize or
 * copy elements as it grows; each insertion and head removal is a
 * constant-time operation. Removal of an arbitrary element, however,
 * requires a linear scan from the head since the list is only singly
 * linked (no {@code previous} pointers).
 *
 * <p>This implementation does <b>not</b> permit {@code null} elements;
 * most mutating methods will throw a {@link NullPointerException} if given
 * a {@code null} argument or a collection containing {@code null}.
 *
 * <p>This class is <b>not</b> thread-safe. If multiple threads access a
 * {@code CustomQueue} instance concurrently, and at least one of the
 * threads modifies it structurally, it must be synchronized externally.
 *
 * <p>The {@link Iterator} (in fact a {@link ListIterator}) returned by
 * {@link #iterator()} is <i>fail-fast</i>: if the queue is structurally
 * modified at any time after the iterator is created, in any way except
 * through the iterator's own {@link ListIterator#remove()},
 * {@link ListIterator#add(Object)}, or {@link ListIterator#set(Object)}
 * methods, the iterator will throw a {@link ConcurrentModificationException}.
 *
 * @author Benjamin Kane
 * LinkedIn - <a href="https://www.linkedin.com/in/benjamin-kane-81149482/"/>
 * GitHub account bk10aao - <a href="https://github.com/bk10aao"/>
 * Repository - <a href="https://github.com/bk10aao/CustomQueueV2"/>
 *
 * @param <E> the type of elements held in this queue
 */
public class CustomQueue<E> implements Queue<E> {

    /**
     * The first node in the chain, i.e. the current head of the queue.
     */
    private Node<E> head;

    /**
     * The last node in the chain, i.e. the current tail of the queue.
     */
    private Node<E> tail;

    /**
     * The number of times this queue has been structurally modified.
     * Used by {@link CustomListIterator} to implement fail-fast behavior.
     */
    private int modCount = 0;

    /**
     * The number of elements currently held in this queue.
     */
    private int size = 0;

    /**
     * Constructs an empty queue.
     */
    public CustomQueue() {
        head = tail = null;
    }

    /**
     * Constructs an empty queue. The {@code size} parameter is accepted
     * for API compatibility (e.g. with a capacity-based constructor) but
     * has no effect, since this linked-list implementation has no fixed
     * or preallocated capacity.
     *
     * @param size must be non-negative; otherwise unused
     * @throws IllegalArgumentException if {@code size} is negative
     */
    public CustomQueue(final int size) {
        if (size < 0)
            throw new IllegalArgumentException();
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    /**
     * Constructs a queue containing the elements of the specified
     * collection, in the order they are returned by the collection's
     * iterator.
     *
     * @param c the collection whose elements are to be placed into this queue
     * @throws NullPointerException if the specified collection is {@code null},
     *         or if any element of the collection is {@code null}
     */
    public CustomQueue(final Collection<? extends E> c) {
        requireNonNull(c);
        for(E e : c)
            requireNonNull(e);
        for(E e : c) {
            Node<E> newNode = new Node<>(e);
            if(head == null)
                head = tail = newNode;
            else {
                tail.next = newNode;
                tail = newNode;
            }
        }
        size = c.size();
    }

    /**
     * Inserts the specified element at the tail of this queue.
     *
     * @param item the element to add
     * @return {@code true} (as specified by {@link Collection#add})
     * @throws NullPointerException if the specified element is {@code null}
     */
    public boolean add(final E item) {
        requireNonNull(item);
        Node<E> newNode = new Node<>(item);
        if(head == null)
            head = tail = newNode;
        else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
        modCount++;
        return true;
    }

    /**
     * Appends all the elements in the specified collection to the tail of
     * this queue, in the order that they are returned by the specified
     * collection's iterator.
     *
     * @param c the collection containing elements to be added to this queue
     * @return {@code true} if this queue changed as a result of the call
     * @throws NullPointerException if the specified collection is {@code null},
     *         or if any element of the collection is {@code null}
     * @throws IllegalArgumentException if the specified collection is this queue
     */
    public boolean addAll(final Collection<? extends E> c) {
        if (c == null)
            throw new NullPointerException();
        if(c == this)
            throw new IllegalArgumentException();
        if (c.isEmpty())
            return false;
        for (E e : c)
            add(e);
        return true;
    }

    /**
     * Removes all the elements from this queue. The queue will be empty
     * after this call returns; the {@code head} and {@code tail}
     * references are cleared so the discarded nodes become eligible for
     * garbage collection.
     */
    public void clear() {
        if (!isEmpty())
            modCount++;
        head = tail = null;
        size = 0;
    }

    /**
     * Returns {@code true} if this queue contains the specified element.
     * More formally, returns {@code true} if and only if this queue
     * contains at least one element {@code e} such that {@code o.equals(e)}.
     * This requires a linear scan from the head of the list.
     *
     * @param o element whose presence in this queue is to be tested
     * @return {@code true} if this queue contains the specified element
     */
    public boolean contains(final Object o) {
        if(o == null || size == 0)
            return false;
        Node<E> current = head;
        if(current.value.equals(o))
            return true;
        while(current.next != null) {
            current = current.next;
            if(current.value.equals(o))
                return true;
        }
        return false;
    }

    /**
     * Returns {@code true} if this queue contains all the elements of the
     * specified collection.
     *
     * @param c the collection to be checked for containment in this queue
     * @return {@code true} if this queue contains all the elements of the
     *         specified collection
     * @throws NullPointerException if the specified collection is {@code null}
     */
    public boolean containsAll(final Collection<?> c) {
        if (c == null)
            throw new NullPointerException();
        if (c.isEmpty())
            return true;
        Set<?> values = (c instanceof Set<?> s) ? s : new HashSet<>(c);
        if (values.size() > this.size())
            return false;
        Set<Object> thisSet = new HashSet<>();
        Node<E> current = head;
        while (current != null) {
            thisSet.add(current.value);
            current = current.next;
        }
        return thisSet.containsAll(values);
    }

    /**
     * Retrieves, but does not remove, the head of this queue.
     *
     * <p>Unlike {@link #peek()}, this method throws an exception if this
     * queue is empty.
     *
     * @return the head of this queue
     * @throws NoSuchElementException if this queue is empty
     */
    public E element() {
        if(size == 0)
            throw new NoSuchElementException();
        return head.value;
    }

    /**
     * Returns {@code true} if this queue contains no elements.
     *
     * @return {@code true} if this queue contains no elements
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns a {@link ListIterator} over the elements in this queue, in
     * proper sequence (from head to tail), starting at position 0.
     *
     * <p>The returned iterator is fail-fast: it will throw a
     * {@link ConcurrentModificationException} if the queue is
     * structurally modified after the iterator is created, except through
     * the iterator's own {@link ListIterator#remove()},
     * {@link ListIterator#add(Object)}, or {@link ListIterator#set(Object)}.
     *
     * @return an iterator over the elements in this queue in proper sequence
     */
    public Iterator<E> iterator() {
        return new CustomListIterator(0);
    }

    /**
     * Inserts the specified element at the tail of this queue.
     *
     * <p>This implementation behaves identically to {@link #add(Object)};
     * since this queue has no fixed capacity limit, this method never
     * returns {@code false}.
     *
     * @param item the element to add
     * @return {@code true} (as specified by {@link Queue#offer})
     * @throws NullPointerException if the specified element is {@code null}
     */
    public boolean offer(final E item) {
        add(item);
        return true;
    }

    /**
     * Retrieves, but does not remove, the head of this queue, or returns
     * {@code null} if this queue is empty.
     *
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    public E peek() {
        return isEmpty() ? null : head.value;
    }

    /**
     * Retrieves and removes the head of this queue, or returns
     * {@code null} if this queue is empty. The former head node is
     * unlinked so it can be garbage collected.
     *
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    public E poll() {
        if(size == 0)
            return null;
        E value = head.value;
        head = head.next;
        if(head == null)
            tail = null;
        size--;
        modCount++;
        return value;
    }

    /**
     * Retrieves and removes the head of this queue.
     *
     * <p>Unlike {@link #poll()}, this method throws an exception if this
     * queue is empty.
     *
     * @return the head of this queue
     * @throws NoSuchElementException if this queue is empty
     */
    public E remove() {
        if(isEmpty())
            throw new NoSuchElementException();
        return poll();
    }

    /**
     * Removes a single instance of the specified element from this queue,
     * if it is present. More formally, removes the first element {@code e}
     * such that {@code o.equals(e)}, if such an element exists, by
     * unlinking its node from the chain. This requires a linear scan from
     * the head of the list.
     *
     * @param o element to be removed from this queue, if present
     * @return {@code true} if this queue contained the specified element
     */
    public boolean remove(final Object o) {
        if(size == 0)
            return false;
        if(head.value.equals(o)) {
            head = head.next;
            if(head == null)
                tail = null;
            size--;
            modCount++;
            return true;
        }
        Node<E> node = head;
        while(node.next != null) {
            if (node.next.value.equals(o)) {
                unlinkNext(node);
                return true;
            }
            node = node.next;
        }
        return false;
    }

    /**
     * Removes from this queue all of its elements that are contained in
     * the specified collection, relinking surrounding nodes as necessary
     * to skip over each removed node.
     *
     * @param c collection containing elements to be removed from this queue
     * @return {@code true} if this queue changed as a result of the call
     * @throws NullPointerException if the specified collection is {@code null}
     */
    public boolean removeAll(final Collection<?> c) {
        requireNonNull(c);
        if (c.isEmpty() || head == null)
            return false;
        Set<?> removeSet = (c instanceof Set<?>) ? (Set<?>) c : new HashSet<>(c);
        boolean modified = false;
        while (head != null && removeSet.contains(head.value)) {
            head = head.next;
            size--;
            modified = true;
        }
        if (head == null) {
            tail = null;
            modCount++;
            return true;
        }
        Node<E> prev = head;
        Node<E> current = head.next;
        while (current != null) {
            if (removeSet.contains(current.value)) {
                prev.next = current.next;
                size--;
                modified = true;
            } else
                prev = current;
            current = current.next;
        }
        if (modified)
            modCount++;
        tail = prev;
        return modified;
    }

    /**
     * Retains only the elements in this queue that are contained in the
     * specified collection. In other words, removes from this queue all
     * of its elements that are not contained in the specified collection,
     * relinking surrounding nodes as necessary to skip over each removed
     * node.
     *
     * @param c collection containing elements to be retained in this queue
     * @return {@code true} if this queue changed as a result of the call
     * @throws NullPointerException if the specified collection is {@code null}
     */
    public boolean retainAll(final Collection<?> c) {
        requireNonNull(c);
        if(c.isEmpty()) {
            boolean modified = !isEmpty();
            clear();
            return modified;
        }
        Set<?> retain = (c instanceof Set<?> s) ? s : new HashSet<>(c);
        boolean modified = false;
        Node<E> node = head;
        Node<E> previous = null;
        while(node != null) {
            Node<E> next = node.next;
            if(!retain.contains(node.value)) {
                if (node == head)
                    head = next;
                else
                    previous.next = next;
                if (node == tail)
                    tail = previous;
                modified = true;
                size--;
            } else
                previous = node;
            node = next;
        }
        if (modified)
            modCount++;
        return modified;
    }

    /**
     * Returns the number of elements in this queue.
     *
     * @return the number of elements in this queue
     */
    public int size() {
        return size;
    }

    /**
     * Returns an array containing all the elements in this queue in
     * proper sequence (from head to tail). The returned array is a
     * newly allocated copy; the caller is free to modify it without
     * affecting this queue.
     *
     * @return an array containing all the elements in this queue
     */
    public Object[] toArray() {
        Object[] array = new Object[size];
        int index = 0;
        for(Node<E> node = head; node != null; node = node.next)
            array[index++] = node.value;
        return array;
    }

    /**
     * Returns an array containing all the elements in this queue in
     * proper sequence (from head to tail); the runtime type of the
     * returned array is that of the specified array.
     *
     * <p>If the queue fits in the specified array, it is returned therein.
     * Otherwise, a new array is allocated with the runtime type of the
     * specified array and the size of this queue. If the queue fits in
     * the specified array with room to spare (i.e., the array has more
     * elements than the queue), the element immediately following the
     * end of the queue is set to {@code null}.
     *
     * @param a the array into which the elements of this queue are to be
     *          stored, if it is big enough; otherwise, a new array of the
     *          same runtime type is allocated for this purpose
     * @param <T> the runtime type of the array to contain the queue
     * @return an array containing all the elements in this queue
     * @throws NullPointerException if the specified array is {@code null}
     */
    public <T> T[] toArray(T[] a) {
        if(a.length < size)
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        int index = 0;
        for(Node<E> x = head; x != null; x = x.next)
            a[index++] = (T) x.value;
        if(a.length > size)
            a[size] = null;
        return a;
    }

    /**
     * Returns a string representation of this queue. The string
     * representation consists of a list of the queue's elements in the
     * order they are returned by its iterator, enclosed in square
     * brackets ({@code "[]"}). Adjacent elements are separated by the
     * characters {@code ", "} (comma and space).
     *
     * @return a string representation of this queue
     */
    public String toString() {
        if(isEmpty())
            return "[]";
        StringBuilder stringBuilder = new StringBuilder("[");
        boolean first = true;
        for(Node<E> x = head; x != null; x = x.next) {
            if(!first)
                stringBuilder.append(", ");
            stringBuilder.append(x.value);
            first = false;
        }
        return stringBuilder.append("]").toString();
    }

    /**
     * Unlinks the node immediately following {@code prev} from the chain
     * (i.e. removes {@code prev.next}), updating {@code tail} if the
     * removed node was the last one. Does nothing if {@code prev} is
     * {@code null} or has no successor.
     *
     * @param prev the node preceding the one to be removed
     */
    private void unlinkNext(final Node<E> prev) {
        if (prev == null || prev.next == null)
            return;
        Node<E> target = prev.next;
        prev.next = target.next;
        if (target == tail)
            tail = prev;
        size--;
        modCount++;
    }

    /**
     * A fail-fast {@link ListIterator} over the elements of the enclosing
     * {@link CustomQueue}. Because the underlying list is only singly
     * linked, backward traversal via {@link #previous()} is supported by
     * walking forward from the head each time the previous node cannot be
     * inferred directly, which makes {@code previous()} an O(n) operation
     * in the general case.
     */
    private class CustomListIterator implements ListIterator<E> {

        /**
         * The node that {@link #next()} will return next, or {@code null} if at the end.
         */
        private Node<E> next;

        /**
         * The node most recently returned by {@link #next()} or
         * {@link #previous()}, or {@code null} if no such node exists
         * (either none has been returned yet, or it was already removed
         * or is no longer eligible for {@link #remove()}/{@link #set(Object)}).
         */
        private Node<E> lastReturned;

        /**
         * The node that {@link #previous()} will return next, or {@code null} if at the start.
         */
        private Node<E> prev;

        /**
         * The index of the element that would be returned by a subsequent call to {@link #next()}.
         */
        private int nextIndex;

        /**
         * The value that {@link CustomQueue#modCount} is expected to have
         * throughout iteration. If this expectation is violated other
         * than through this iterator's own mutating methods, the
         * iterator has detected concurrent modification.
         */
        private int expectedModCount = modCount;

        /**
         * Constructs a list iterator positioned so that a subsequent call
         * to {@link #next()} would return the element at the specified
         * index.
         *
         * @param index the index of the first element to be returned by
         *              {@link #next()} (may equal the current queue size,
         *              in which case the iterator starts at the end)
         */
        CustomListIterator(final int index) {
            nextIndex = index;
            if(size == 0) {
                next = null;
                prev = null;
                return;
            }
            if(index == 0) {
                next = head;
                prev = null;
                return;
            }
            if(index == size) {
                next = null;
                prev = head;
                for(int i = 1; i < size; i++)
                    prev = prev.next;
                return;
            }
            Node<E> current = head;
            for(int i = 0; i < index; i++)
                current = current.next;
            next = current;
            prev = getNodeAt(index - 1);
        }

        /**
         * Inserts the specified element into the list immediately before
         * the element that would be returned by {@link #next()}, if any,
         * and after the element that would be returned by
         * {@link #previous()}, if any. The new element is inserted before
         * the implicit cursor: a subsequent call to {@code next} would be
         * unaffected, and a subsequent call to {@code previous} would
         * return the new element.
         *
         * @param e the element to insert
         * @throws ConcurrentModificationException if the queue was
         *         structurally modified since this iterator was created,
         *         other than through this iterator's own mutating methods
         * @throws NullPointerException if the specified element is {@code null}
         */
        public void add(final E e) {
            checkForComodification();
            requireNonNull(e);
            Node<E> newNode = new Node<>(e);
            if(next == null)
                if(tail == null)
                    head = tail = newNode;
                else {
                    tail.next = newNode;
                    tail = newNode;
                }
            else if(next == head) {
                newNode.next = head;
                head = newNode;
            } else {
                prev.next = newNode;
                newNode.next = next;
            }
            lastReturned = null;
            prev = newNode;
            next = newNode.next;
            nextIndex++;
            size++;
            modCount++;
            expectedModCount = modCount;
        }

        /**
         * Returns {@code true} if this list iterator has more elements
         * when traversing the list in the forward direction.
         *
         * @return {@code true} if there is a next element
         */
        public boolean hasNext() {
            return next != null;
        }

        /**
         * Returns {@code true} if this list iterator has more elements
         * when traversing the list in the reverse direction.
         *
         * @return {@code true} if there is a previous element
         */
        public boolean hasPrevious() {
            return prev != null;
        }

        /**
         * Returns the next element in the list and advances the cursor
         * position.
         *
         * @return the next element in the list
         * @throws ConcurrentModificationException if the queue was
         *         structurally modified since this iterator was created,
         *         other than through this iterator's own mutating methods
         * @throws NoSuchElementException if there is no next element
         */
        public E next() {
            checkForComodification();
            if(!hasNext())
                throw new NoSuchElementException();
            lastReturned = next;
            prev = lastReturned;
            next = next.next;
            nextIndex++;
            return lastReturned.value;
        }

        /**
         * Returns the index of the element that would be returned by a
         * subsequent call to {@link #next()}.
         *
         * @return the index of the next element, or the list size if the
         *         cursor is at the end of the list
         */
        public int nextIndex() {
            return nextIndex;
        }

        /**
         * Returns the previous element in the list and moves the cursor
         * position backwards. Since the underlying list has no backward
         * links, locating the node before the returned one (for a
         * subsequent {@code previous()} call) may require an O(n) scan
         * from the head via {@link #getNodeAt(int)}.
         *
         * @return the previous element in the list
         * @throws ConcurrentModificationException if the queue was
         *         structurally modified since this iterator was created,
         *         other than through this iterator's own mutating methods
         * @throws NoSuchElementException if there is no previous element
         */
        public E previous() {
            checkForComodification();
            if(!hasPrevious())
                throw new NoSuchElementException();
            lastReturned = prev;
            next = lastReturned;
            prev = (nextIndex == 1) ? null : getNodeAt(nextIndex - 2);
            nextIndex--;
            return lastReturned.value;
        }

        /**
         * Returns the index of the element that would be returned by a
         * subsequent call to {@link #previous()}.
         *
         * @return the index of the previous element, or {@code -1} if the
         *         cursor is at the beginning of the list
         */
        public int previousIndex() {
            return nextIndex - 1;
        }

        /**
         * Removes from the underlying queue the last element returned by
         * {@link #next()} or {@link #previous()}, unlinking its node from
         * the chain and updating {@code head}/{@code tail} as necessary.
         *
         * @throws ConcurrentModificationException if the queue was
         *         structurally modified since this iterator was created,
         *         other than through this iterator's own mutating methods
         * @throws IllegalStateException if neither {@code next} nor
         *         {@code previous} has been called, or {@code remove} or
         *         {@code add} has already been called after the last call
         *         to {@code next} or {@code previous}
         */
        public void remove() {
            checkForComodification();
            if(lastReturned == null)
                throw new IllegalStateException();
            if(lastReturned == head) {
                head = head.next;
                if (head == null)
                    tail = null;
            } else {
                Node<E> before = (prev == lastReturned) ? getNodeAt(nextIndex - 2) : prev;
                if(before == null)
                    throw new IllegalStateException();
                before.next = lastReturned.next;
                if(lastReturned == tail)
                    tail = before;
            }
            if(next == lastReturned)
                next = lastReturned.next;
            else
                nextIndex--;
            lastReturned = null;
            size--;
            modCount++;
            expectedModCount = modCount;
        }

        /**
         * Replaces the last element returned by {@link #next()} or
         * {@link #previous()} with the specified element.
         *
         * @param e the element with which to replace the last element
         *          returned by {@code next} or {@code previous}
         * @throws ConcurrentModificationException if the queue was
         *         structurally modified since this iterator was created,
         *         other than through this iterator's own mutating methods
         * @throws IllegalStateException if neither {@code next} nor
         *         {@code previous} has been called, or {@code remove} or
         *         {@code add} has already been called after the last call
         *         to {@code next} or {@code previous}
         * @throws NullPointerException if the specified element is {@code null}
         */
        public void set(final E e) {
            checkForComodification();
            if(lastReturned == null)
                throw new IllegalStateException();
            requireNonNull(e);
            lastReturned.value = e;
        }

        /**
         * Checks that the enclosing queue has not been structurally
         * modified since this iterator was created (other than through
         * this iterator's own mutating methods).
         *
         * @throws ConcurrentModificationException if a structural
         *         modification is detected
         */
        private void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }

        /**
         * Returns the node at the specified index by walking forward
         * from the head, or {@code null} if the index is out of range.
         * This is an O(n) operation.
         *
         * @param index the index of the desired node
         * @return the node at the specified index, or {@code null} if
         *         {@code index} is negative or not less than the current size
         */
        private Node<E> getNodeAt(final int index) {
            if (index < 0 || index >= size)
                return null;
            Node<E> node = head;
            for (int i = 0; i < index; i++)
                node = node.next;
            return node;
        }
    }

    /**
     * A single node in the singly linked chain backing this queue,
     * holding an element value and a reference to the next node.
     *
     * @param <E> the type of the value held by this node
     */

    private static class Node<E> {

        /**
         * Reference to the next node in the chain, or {@code null} if this is the last node.
         */
        private Node<E> next;

        /**
         * The element value held by this node.
         */
        private E value;

        /**
         * Constructs a new node holding the specified value, with no
         * successor.
         *
         * @param value the element value to store in this node
         */
        public Node(E value) {
            this.value = value;
        }
    }
}
