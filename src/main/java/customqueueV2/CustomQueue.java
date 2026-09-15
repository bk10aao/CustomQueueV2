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

public class CustomQueue<E> implements Queue<E> {

    private Node<E> head;
    private Node<E> tail;
    private int modCount = 0;

    private int size = 0;

    public CustomQueue() {
        head = tail = null;
    }

    public CustomQueue(final int size) {
        if (size < 0)
            throw new IllegalArgumentException();
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

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

    public void clear() {
        if (!isEmpty())
            modCount++;
        head = tail = null;
        size = 0;
    }

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

    public E element() {
        if(size == 0)
            throw new NoSuchElementException();
        return head.value;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Iterator<E> iterator() {
        return new CustomListIterator(0);
    }

    public boolean offer(final E item) {
        add(item);
        return true;
    }

    public E peek() {
        return isEmpty() ? null : head.value;
    }

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

    public E remove() {
        if(isEmpty())
            throw new NoSuchElementException();
        return poll();
    }

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

    public int size() {
        return size;
    }

    public Object[] toArray() {
        Object[] array = new Object[size];
        int index = 0;
        for(Node<E> node = head; node != null; node = node.next)
            array[index++] = node.value;
        return array;
    }

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

    private class CustomListIterator implements ListIterator<E> {
        private Node<E> next;
        private Node<E> lastReturned;
        private Node<E> prev;

        private int nextIndex;
        private int expectedModCount = modCount;

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

        public boolean hasNext() {
            return next != null;
        }

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

        public boolean hasPrevious() {
            return prev != null;
        }

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

        public int nextIndex() {
            return nextIndex;
        }

        public int previousIndex() {
            return nextIndex - 1;
        }

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

        public void set(final E e) {
            checkForComodification();
            if(lastReturned == null)
                throw new IllegalStateException();
            requireNonNull(e);
            lastReturned.value = e;
        }

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

        private void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }

        private Node<E> getNodeAt(final int index) {
            if (index < 0 || index >= size)
                return null;
            Node<E> node = head;
            for (int i = 0; i < index; i++)
                node = node.next;
            return node;
        }
    }

    private static class Node<E> {
        Node<E> next;
        E value;
        public Node(E value) {
            this.value = value;
        }
    }
}
