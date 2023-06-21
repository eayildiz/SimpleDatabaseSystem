import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**This class is collection of rows. It will store rows in a singly linked list.
 */
public class Table{
    //CLASS VARIABLES
    public SinglyLinkedList<Row> rowsInTable;
    public BinarySearchTree indexedFirstColumn;
    public String header;
    public int numberOfRows;
    //CONSTRUCTORS
    public Table(String pathFile, String header) throws FileNotFoundException{
        this.header = header;
        numberOfRows = 0;
        Scanner readCSV = new Scanner(new File(pathFile));
        rowsInTable = new SinglyLinkedList<>();
        //To keep tokens of input seperated.
        String[] inputRow;
        //To keep a sorted row LL.
        SinglyLinkedList<Row> sortedRowByFirstColumn = new SinglyLinkedList<>();
        //Keeping new row for different processes.
        Row newRow;
        while(readCSV.hasNextLine()){
            inputRow = readCSV.nextLine().split(",");
            newRow = new Row(inputRow);
            rowsInTable.add(newRow);
            sortedRowByFirstColumn = createSortedFirstColumn(sortedRowByFirstColumn, newRow, numberOfRows);
            numberOfRows++;
        }
        Row[] finalOrderedRows = new Row[numberOfRows];
        SinglyLinkedList<Row>.Node iteratorNode = sortedRowByFirstColumn.headNode;
        for(int index = 0; index < finalOrderedRows.length; index++){
            finalOrderedRows[index] = iteratorNode.element;
            iteratorNode = iteratorNode.nextNode;
        }
        indexedFirstColumn = new BinarySearchTree(finalOrderedRows);
    }
    //METHODS
    /**Calling printRow for every single row in a table.
     */
    public void printTable(){
        SinglyLinkedList<Row>.Node tempNode = rowsInTable.headNode;
        while(tempNode != null){
            tempNode.element.printRow();
            tempNode = tempNode.nextNode;
        }
    }
    public SinglyLinkedList<Row> createSortedFirstColumn(SinglyLinkedList<Row> halfSortedLL, Row newRow, int numberOfElements){
        SinglyLinkedList<Row>.Node temp = halfSortedLL.headNode;
        while(temp != null){
            if(temp.element.elementsOfRow[0].compareTo(rowsInTable.tailNode.element.elementsOfRow[0]) >= 0){
                halfSortedLL.addNext(temp, newRow);
                return halfSortedLL;
            }
            temp = temp.nextNode;
        }
        halfSortedLL.add(rowsInTable.tailNode.element);
        return halfSortedLL;
    }
}