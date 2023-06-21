public class BinarySearchTree {
    //Inner node class for binary search tree.
    public class Node{
        //CLASS VARIABLES
        Row element;
        Node nextNodeThroughDepth;
        Node left;
        Node right;
        boolean canFertilize;
        //CONSTRUCTORS
        public Node(Row newRow, Boolean isFertilize){
            left = null;
            right = null;
            nextNodeThroughDepth = null;
            element = newRow;
            canFertilize = isFertilize;
        }
    }
    //CLASS VARIABLES
    public Node root;
    //CONSTRUCTORS
    public BinarySearchTree(Row[] orderedArray){
        root = deployingBST(orderedArray, 0, orderedArray.length - 1, root);
    }
    //METHODS
    /**Forming new BST from sorted array recursively.
     * @param sourceArray
     * @param startIndex
     * @param endIndex
     * @param root
     * @return newArray
     */
    public Node deployingBST(Row[] sourceArray, int startIndex, int endIndex, Node root){
        //Base case.
        if(startIndex > endIndex){
            return root;
        }
        int mid = (startIndex + endIndex) / 2;
        //Construct a new node from the middle element and assign it to the root
        root = new Node(sourceArray[mid], true);
        //Find other equivalent nodes to add new duplicate nodes to main fertilied node.
        Node tempIterator = root;
        int smaller;
        int bigger;
        for(smaller = 1; (mid - smaller >= 0) && (sourceArray[mid] == sourceArray[mid - smaller]); smaller--){
            tempIterator.nextNodeThroughDepth = new Node(sourceArray[mid - smaller], false);
            tempIterator = tempIterator.nextNodeThroughDepth;
        }
        for(bigger = 1; (mid + bigger < sourceArray.length) && (sourceArray[mid] == sourceArray[mid + bigger]); bigger++){
            tempIterator.nextNodeThroughDepth = new Node(sourceArray[mid + bigger], false);
            tempIterator = tempIterator.nextNodeThroughDepth;
        }
        //Left subtree of the root will be formed by keys less than middle element.
        root.left = deployingBST(sourceArray, startIndex, mid - smaller, root.left);
        //Right subtree of the root will be formed by keys more than the middle element.
        root.right = deployingBST(sourceArray, mid + bigger, endIndex, root.right);
        return root;
    }
    /**Finding the value from BST.
     * @param targetValue
     * @return
     */
    public Node findingRows(String targetValue){
        Node temp = root;
        while(temp != null){
            if(temp.element.elementsOfRow[0].compareTo(targetValue) == 0){
                break;
            }
            else if(temp.element.elementsOfRow[0].compareTo(targetValue) < 0){
                temp = temp.left;
            }
            else{
                temp = temp.right;
            }
        }
        return temp;
    }
    //TEST METHODS
    /**
     * @param aNode
     */
    public void printBST(Node aNode){
        if(aNode != null){
            System.out.println(aNode.element.elementsOfRow[0]);
            printBST(aNode.left);
            printBST(aNode.right);
        }
    }
}