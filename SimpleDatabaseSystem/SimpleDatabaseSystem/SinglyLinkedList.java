/* Basic linked list data type that hold Node inner class inside.
 * ET stands for element type.
 */
public class SinglyLinkedList<ET>{
    /* Inner class that have generic type value and reference to the next node.
     */
    public class Node{
        //Node elements
        public ET element;
        public Node nextNode;
        //Constructors
        public Node(ET inputValue){
            element = inputValue;
            nextNode = null;
        }
    }
    //CLASS VARIABLES
    public Node headNode;
    public Node tailNode;
    public int size;
    //CONSTRUCTORS
    public SinglyLinkedList(){
        headNode = null;
        tailNode = headNode;
    }
    //METHODS
    /**Adding a node to the end of the list.
     * @param inputValue
     */
    public void add(ET inputValue){
        if(size == 0){
            headNode = new Node(inputValue);
            tailNode = headNode;
        }
        else{
            tailNode.nextNode = new Node(inputValue);
            tailNode = tailNode.nextNode;
        }
        size++;
    }
    /**Adding a node after a node.
     * @param currentNode
     * @param futureNode
     */
    public void addNext(Node currentNode, ET futureNodeValue){
        Node futureNode = new Node(futureNodeValue);
        futureNode.nextNode = currentNode.nextNode;
        currentNode.nextNode = futureNode;
        size++;
    }
    /**This method prints linked list. Output look like a row of table.
     * This method is called by only Row to prevent printing adresses. It can be changed with adding parameter Node and iterating until finding null
     * so that even table can evoke this method. But for now let's keep things simple.
     */
    public void printSLL(){
        SinglyLinkedList<ET>.Node iteratorNode = headNode; 
        while(iteratorNode != null){
            System.out.print(iteratorNode.element);
            if(iteratorNode.nextNode != null){
                System.out.print("\t");
            }
            else{
                System.out.print("\n");
            }
            iteratorNode = iteratorNode.nextNode;
        }
    }
}