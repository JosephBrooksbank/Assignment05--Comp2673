import java.util.Stack;

/**
 * Class for the filesystem for assignment 05
 *
 * @author Joseph Brooksbank
 * @version 5/22/2018
 *
 * Extras added in this file:
 * Rather than just a carrot (">") for a prompt, I decided to use a more traditional bash prompt as "user@machine $".
 *
 * cd -- added "/" root path accessible from anywhere
 * echo -- echos input
 * su -- Switch User, changes to other user on machine
 * users -- a stack of users, which keeps track of the current user elevation
 * exit -- leaves the current shell
 */
class FileSystem {
    Stack<String> users;
    private final FileSystemNode root;
    private FileSystemNode cwd;

    String machine;
    String elevation;

    /**
     * Constructor to create an empty filesystem
     */
    FileSystem() {
        root = new FileSystemNode("/", true);
        cwd = root;
        users = new Stack<>();
        users.push(System.getProperty("user.name"));
        machine = "@javaVirtualShell";
        elevation = " $";
    }

    /**
     * Changes current working directory to the given directory
     *
     * @param dir_name The directory to change to
     */
    void cd(String dir_name) {

        // Special cases
        // TODO add other useful cases like "-"
        if (dir_name.equals(".")) {
            return;
        }

        if (dir_name.equals("..")) {
            if (cwd.parent != null)
                cwd = cwd.parent;
            else {
                System.out.println("That directory doesn't exist");
            }
            return;
        }
        // Added a root path for cd
        if (dir_name.equals("/")) {
            cwd = root;
            return;
        }

        for (FileSystemNode aNode : cwd.getChildren()) {
            if (aNode.name.equals(dir_name)) {
                if (aNode.isDirectory()) {
                    cwd = aNode;
                    return;
                }
                else {
                    System.out.println("Not a directory!");
                    return;
                }
            }

        }
        System.out.println("Directory not found.");
    }

    /**
     * Lists all contents of current working directory
     */
    void ls() {
        for (FileSystemNode aNode : cwd.getChildren()) {
            String type = (aNode.isDirectory()) ? "(d)" : "(f)";
            System.out.println(type + " " + aNode.name);
        }
    }

    /**
     * Helper function for creation of files and directories
     * @param name  The name of the file/directory which may exist
     * @return      Whether it exists or not
     */
    private boolean exists(String name){

        for (FileSystemNode aNode : cwd.getChildren()) {
            if (aNode.name.equals(name))
                return true;
            }
            return false;
        }

    /**
     * Makes a directory of the name given
     *
     * @param dir_name Name of the directory to create
     */
    void mkdir(String dir_name) {
        if (!exists(dir_name))
            cwd.add(new FileSystemNode(dir_name, true));
        else {
            System.out.println("Already exists.");
        }
    }

    /**
     * Touches a file to create an empty file that location
     *
     * @param file_name The file to create
     */
    void touch(String file_name) {
        boolean exists = false;
        if (!exists(file_name)) {
            cwd.add(new FileSystemNode(file_name, false));
        } else {
            System.out.println("Already exists.");
        }
    }

    /**
     * Prints current working directory
     */
    void pwd() {
        StringBuilder sb = new StringBuilder();
        FileSystemNode cursor = cwd;
        System.out.print("/");
        while (cursor.hasParent()) {
            sb.insert(0, cursor.name + "/");
            cursor = cursor.parent;
        }
        System.out.println(sb.toString());
    }

    /**
     * Removes file at that location
     *
     * @param file_name File to remove
     */
    void rm(String file_name) {
        for (FileSystemNode aNode : cwd.getChildren()) {
            if (aNode.name.equals(file_name)) {
                if (aNode.isDirectory()) {
                    System.out.println("This is a directory, try using rmdir");
                    return;
                } else {
                    cwd.remove(aNode);
                    return;
                }
            }

        }
        System.out.println("File not found");
    }

    /**
     * Remove an empty folder of that name
     *
     * @param dir_name The name of the folder to remove
     */
    void rmdir(String dir_name) {
        for (FileSystemNode aNode : cwd.getChildren()) {
            if (aNode.name.equals(dir_name)) {
                if (aNode.isDirectory()) {
                    if (!aNode.hasChildren()) {
                        cwd.remove(aNode);
                        return;
                    } else {
                        System.out.println("This has items in it still, can't delete.");
                        return;
                    }
                } else {
                    System.out.println("This is a file, try using rm");
                    return;
                }
            }
        }
        System.out.println("Directory not found");
    }



    /**
     * Prints out the directory tree from the cwd
     */
    void tree() {
        // Base case: path can continue no longer:
        if (!cwd.hasChildren()) {
            return;
        }

        // For every child, recursively print the tree
        for (FileSystemNode aNode : cwd.getChildren()) {

            // Using a Stringbuilder to build the path back up for each entry
            StringBuilder sb = new StringBuilder();

            FileSystemNode cursor = aNode;
            while (cursor.hasParent()) {
                // Whether to add a "/" to denote its a directory
                if (cursor.isDirectory())
                    sb.insert(0, cursor.name + "/");
                else
                    sb.insert(0, cursor.name);
                cursor = cursor.parent;
            }
            sb.insert(0, "./");
            System.out.println(sb.toString());
            // Working on the nodes recursively
            cwd = aNode;
            tree();
            cwd = cwd.parent;
        }
    }

    public void exit(){
        if (users.size() <= 1){
            System.exit(0);
        } else
        users.pop();
    }

    // TODO add piping and file editing

    /**
     * Echo function, echos text given to it. Used for a multitude of things in shells, but not very helpful here.
     *
     * @param echo_text The text to echo
     */
    void echo(String echo_text) {
        System.out.println(echo_text);
    }

    /**
     * Method to switch user, with added root warning
     * @param user  The user to switch to
     */
    void su(String user) {
        if (user.equals("root")) {
            System.out.println("\nWe trust you have recieved the usual lecture from the local System \n" +
                    "Administrator. It usually boils down to these three things: \n \n" +
                    "" +
                    "#1) Respect the privacy of others. \n" +
                    "#2) Think before you type. \n" +
                    "#3) With great power comes great responsibility. \n" +
                    "Root user autologin...\n");
            users.push("root");
            this.elevation = " #";
        } else {
            users.push(user);
        }
    }


}
