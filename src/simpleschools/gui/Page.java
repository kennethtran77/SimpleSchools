package simpleschools.gui;

import javafx.scene.Node;
import simpleschools.SimpleSchools;

/**
 *
 * @author Kenneth Tran
 */
public abstract class Page {
    
    private final SimpleSchools main;
    
    protected Node component;
    
    protected Page(SimpleSchools main) {
        this.main = main;
    }
    
    public Node getComponent() {
        return this.component;
    }
    
    public SimpleSchools getMain() {
        return this.main;
    }
    
}