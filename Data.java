package Project;

import java.io.Serializable;

/**
 * Created by Markus on 2016-12-09.
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
