import java.util.LinkedList;
import java.util.List;

class FileSystemNode {

    final String name;
    /**
     * Type of node, true is directory
     */
    private final boolean type;
    private final List<FileSystemNode> children;
    FileSystemNode parent;

    FileSystemNode(String name, boolean type) {
        this.parent = null;
        this.name = name;
        this.type = type;
        this.children = new LinkedList<>();
    }

    boolean isDirectory() {
        return type;
    }

    boolean hasChildren() {
        return !this.children.isEmpty();
    }

    List<FileSystemNode> getChildren() {
        return children;
    }

    boolean hasParent() {
        return parent != null;
    }

    void add(FileSystemNode node) {
        children.add(node);
        node.parent = this;
    }

    void remove(FileSystemNode node) {
        children.remove(node);
        node.parent = null;
    }


}
