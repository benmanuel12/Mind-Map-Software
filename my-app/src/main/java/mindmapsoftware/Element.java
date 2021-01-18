package mindmapsoftware;

import java.io.Serializable;

public abstract class Element implements Serializable {

    int id;
    public int getId(){
        return this.id;
    };

    public void setId(int id){
        this.id = id;
    };
}
