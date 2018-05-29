public class MyHashtable<K extends Comparable<K>, V>{
    private static int size = 100;

    private AVLTree<K,V>[] table;

    public MyHashtable(){
//      AVLTree 배열 생성
        table = new AVLTree[size];
        for (int i=0; i<size; i++){
            table[i] = new AVLTree<K,V>();
        }
    }

    public void insert(K key, V value){
        table[hash(key)].insert(key, value);
    }

    public String IndexSerach(int index){
        String s = table[index].preorder();
        if (s.equals(""))
            return "EMPTY";
        else
            return s;
    }

    public LinkedListNode<K, V> patternSearch(K key){
        return table[hash(key)].search(key);
    }

    private int hash(Object key) {
        if (key instanceof String) {
            String s = (String) key;
            int result = 0;
            for (int i = 0; i < s.length(); i++)
                result += (int) s.charAt(i);
            return result % size;
        }
        return 0;
    }

}
