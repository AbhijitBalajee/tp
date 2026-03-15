package seedu.duke;

public class HelpCommand extends Command {

    @Override
    public void execute() {
        System.out.println("Available commands:");
        System.out.println("add d/<desc> a/<amount> c/<category>  -> add an expense");
        System.out.println("delete <index>                        -> delete an expense");
        System.out.println("list                                  -> list all expenses");
        System.out.println("total                                 -> show total expenses");
        System.out.println("budget <amount>                       -> set monthly budget");
        System.out.println("remaining                             -> show remaining balance");
        System.out.println("help                                  -> show help information");
        System.out.println("bye                                   -> exit the program");
    }
}