public class AVLTree<K extends Comparable<K>, V> {
    private LinkedListNode<K,V> root;

    AVLTree(){
        root = null;
    }

    public void insert(K key, V value) {
        root = insertHelper(root, key, value);
    }

    public LinkedListNode<K,V> search(K key){
        return searchHelper(root, key);
    }

    public LinkedListNode<K,V> searchHelper(LinkedListNode<K, V> node, K key){
//      key가 없는 경우
        if (node == null)
            return new LinkedListNode<>(key);
        if (key.compareTo(node.key) == 0)
            return node;
        else if (key.compareTo(node.key) > 0)
            return searchHelper(node.right, key);
        else
            return searchHelper(node.left, key);
    }

//  preorder로 순회한 결과 반환
    public String preorder(){
        String result = preorderHelper(root);
        if (result.length() != 0)
            return result.substring(0, result.length()-1);
        return result;
    }

    public String preorderHelper(LinkedListNode node){
        String result = "";
        if (node == null)
            return "";
        result += node.key + " ";
        result += preorderHelper(node.left);
        result += preorderHelper(node.right);
        return result;
    }

    public LinkedListNode insertHelper(LinkedListNode<K,V> root, K key, V value){
//      새로운 노드 생성
        if (root == null) {
            LinkedListNode newNode = new LinkedListNode(key);
            newNode.add(value);
            return newNode;
        }
//      현재 리스트에 삽입
        if (root.key.compareTo(key) == 0){
            root.add(value);
        }
//      right tree에 삽입
        else if(key.compareTo(root.key) > 0){
            root.right = insertHelper(root.right, key, value);
//          height update
            root.rightHeight = Math.max(root.right.leftHeight, root.right.rightHeight) + 1;
//          balance가 깨진 경우
            if(root.rightHeight - root.leftHeight == 2){
                if(key.compareTo((K) root.right.key) > 0)
                    root = rotateLeft(root);
                else
                    root = rotateRightLeft(root);
            }
        }
//      left tree에 삽입
        else{
            root.left = insertHelper(root.left, key, value);
            root.leftHeight =  Math.max(root.left.leftHeight, root.left.rightHeight) + 1;

//          balance가 깨진경우
            if (root.leftHeight - root.rightHeight == 2){
                if(key.compareTo((K) root.left.key) < 0)
                    root = rotateRight(root);
                else
                    root = rotateLeftRight(root);
            }
        }
        return root;
    }

    private LinkedListNode<K,V> rotateLeft(LinkedListNode root) {
        LinkedListNode<K,V> tmp = root.right.left;
        root.rightHeight = root.right.leftHeight;
        root.right.leftHeight = Math.max(root.leftHeight, root.rightHeight) + 1;
        root.right.left = root;
        root = root.right; // new root
        root.left.right = tmp;
        return root;
    }

//  Left rotate 의 mirror
    private LinkedListNode<K,V> rotateRight(LinkedListNode root) {
        LinkedListNode<K,V> tmp = root.left.right;
        root.leftHeight = root.left.rightHeight;
        root.left.rightHeight = Math.max(root.leftHeight, root.rightHeight) + 1;
        root.left.right = root;
        root = root.left; // new root
        root.right.left = tmp;
        return root;
    }

    private LinkedListNode<K,V> rotateLeftRight(LinkedListNode root) {
        root.left = rotateLeft(root.left);
        root = rotateRight(root);
        return root;
    }

    private LinkedListNode<K,V> rotateRightLeft(LinkedListNode root) {
        root.right = rotateRight(root.right);
        root = rotateLeft(root);
        return root;
    }

}
