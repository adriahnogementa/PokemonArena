import java.io.Serializable;

public class Pokemon implements Serializable {
    String name;
    int hp;
    int attack;


    public Pokemon(String name, int hp, int attack) {
        this.name = name;
        this.hp = hp;
        this.attack = attack;
    }

    public String getName() {
        return this.name;
    }

    public int getHp() {
        return hp;
    }

    public int getAttack() {
        return attack;
    }

}
