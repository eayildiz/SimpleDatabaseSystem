public class UnrecognizableCommand extends Exception{
    public UnrecognizableCommand(){
        System.out.println("Unrecognizable Command!");
    }
    public UnrecognizableCommand(String errorMessage){
        System.out.println(errorMessage);
    }
}
