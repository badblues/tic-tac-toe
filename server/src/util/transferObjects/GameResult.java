package util.transferObjects;

import java.io.Serializable;

public class GameResult implements Serializable {

    String name1;
    String name2;
    String result1;
    String result2;

    public GameResult(String name1, String name2) {
        this.name1 = name1;
        this.name2 = name2;
    }

    public void setResults(String result1, String result2) {
        this.result1 = result1;
        this.result2 = result2;
    }


    public String getName1() {
        return name1;
    }

    public String getResult1() {
        return result1;
    }

    public String getName2() {
        return name2;
    }

    public String getResult2() {
        return result2;
    }

}
