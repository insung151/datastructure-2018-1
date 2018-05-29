//  AVLTree의 Node로 쓰이는 클래스
public class LinkedListNode<K extends Comparable, V>  {
    Node<V> head;
    K key;
    LinkedListNode<K, V> left, right;
    int leftHeight, rightHeight;
    int size;

    public LinkedListNode(K key){
        head = null;
        this.key = key;
    }
    public boolean isEmpty(){
        return size == 0;
    }

    public int size(){
        return size;
    }

//  맨 끝에 item 삽입
    public void add(V item){
        if (head == null)
            head = new Node<>(item);
        else{
            Node cur = head;
            while (cur.getNext() != null){
                cur = cur.getNext();
            }
            cur.setNext(new Node(item));
        }
        size++;
    }

    public V first(){
        if (isEmpty())
            throw new IndexOutOfBoundsException();
        else
            return head.getItem();
    }

    public void removeAll(){
        head = new Node<>(null);
    }

    public void remove(V item){
        if (head == null)
            return;
        if (head.getItem() == item)
            head = head.getNext();

        Node<V> prev = head;
        while(prev.getNext().getItem() != item){
            prev = prev.getNext();
        }
        if (prev.getNext().getItem() == item)
            prev.setNext(prev.getNext().getNext());
    }

//  copy form java API
    public int indexOf(Object o) {
        int index = 0;
        if (isEmpty())
            return -1;
        if (o == null) {
            for (Node<V> x = head; x != null; x = x.getNext()) {
                if (x.getItem() == null)
                    return index;
                index++;
            }
        } else {
            for (Node<V> x = head; x != null; x = x.getNext()) {
                if (o.equals(x.getItem()))
                    return index;
                index++;
            }
        }
        return -1;
    }

//  copy from java API
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    public V get(int index){
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
        Node<V> cur = head;
        for (int i=0; i<index; i++)
            cur = cur.getNext();
        return cur.getItem();
    }

    @Override
    public String toString() {
        if (isEmpty())
            return "";
        String result = head.getItem().toString();
        Node<V> cur = head.getNext();
        while (cur != null){
            result += " " + cur.getItem().toString();
            cur = cur.getNext();
        }
        return result;
    }
}

class Node<T> {
    private T item;
    private Node<T> next;

    public Node(T obj) {
        this.item = obj;
        this.next = null;
    }

    public Node(T obj, Node<T> next) {
        this.item = obj;
        this.next = next;
    }

    public final T getItem() {
        return item;
    }

    public final void setItem(T item) {
        this.item = item;
    }

    public final void setNext(Node<T> next) {
        this.next = next;
    }

    public Node<T> getNext() {
        return this.next;
    }
}