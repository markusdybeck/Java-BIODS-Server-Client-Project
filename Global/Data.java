package Project.Global;

import java.io.Serializable;

/**
 * The Data class simply is a class for simplifying message parsing.
 *
 * @author Markus Dybeck
 * @since 2016-12-09
 * @version 1.0
 */

public class Data implements Serializable {

    private String action;
    private Object object;

    public Data(String str, Object obj) {
        this.action = str;
        this.object = obj;
    }

    public String getAction() { return this.action;}
    public Object getObject() { return this.object;}

}
