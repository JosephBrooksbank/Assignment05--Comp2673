import java.util.Scanner;

/**
 * Driver class for Assignment 5
 *
 * @author Joseph Brooksbank
 * @version 5/22/2018
 *
 * NOTE: I wasn't happy with the total capabilities of this "shell", so I added a couple of more.
 * cd / -- returns to root
 * rm -rf -- removes folder
 * echo -- echos text
 * su  -- switches user to specified user 
 * exit -- exits the current instance of the shell, meaning if you have started new instances (with su) than it will
 * simply exit the latest one and move down a level.
 */
class Driver {
    public static void main(String[] args) {

        FileSystem fileSystem = new FileSystem();
        Scanner stdin = new Scanner(System.in);

        while (true) {

            System.out.print(fileSystem.users.peek() + fileSystem.machine + fileSystem.elevation);
            String tmp = stdin.next();

            switch (tmp) {
                case "exit":
                    fileSystem.exit();
                    break;
                case "pwd":
                    fileSystem.pwd();
                    break;
                case "cd":
                    fileSystem.cd(stdin.next());
                    break;
                case "ls":
                    fileSystem.ls();
                    break;
                case "mkdir":
                    fileSystem.mkdir(stdin.next());
                    break;
                case "rm":

                    // Added the ability to use the "-rf" tag with rm because I'm more used to that than rmdir
                    tmp = stdin.next();
                    if (tmp.equals("-rf"))
                        fileSystem.rmdir(stdin.next());
                    else
                        fileSystem.rm(tmp);

                    break;
                case "rmdir":
                    fileSystem.rmdir(stdin.next());
                    break;
                case "touch":
                    fileSystem.touch(stdin.next());
                    break;
                case "tree":
                    fileSystem.tree();
                    break;
                case "echo":
                    fileSystem.echo(stdin.next());
                    break;
                case "su":
                    fileSystem.su(stdin.next());
                    break;
                default:
                    stdin.nextLine();
                    break;
            }


        }
    }
}
