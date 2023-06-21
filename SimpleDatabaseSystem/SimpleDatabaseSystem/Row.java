/**This class simply put the elements of that row in a linked list.
 */
public class Row {
    //CLASS VARIABLES
    public String[] elementsOfRow;
    //CONSTRUCTORS
    public Row(String[] inputRow){
        elementsOfRow = inputRow;
    }
    //METHODS
    /**Evoke printSLL() to print a row.
     */
    public void printRow(){
        for(int index = 0; index < elementsOfRow.length; index++){
            System.out.print(elementsOfRow[index]);
            if(index == elementsOfRow.length - 1){
                System.out.print("\n");
            }
            else{
                System.out.print("\t");
            }
        }
    }
}