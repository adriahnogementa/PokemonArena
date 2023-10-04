

public class Pokemon{
    String name;
    int hp;
    int attack;
    int initiative;
    int dodgeChance;
    boolean isDead = false;


    public Pokemon(String name, int hp, int attack, int initiative, int dodgeChance) {
        this.name = name;
        this.hp = hp;
        this.attack = attack;
        this.initiative = initiative;
        this.dodgeChance = dodgeChance;
    }

    public String getName() {
        return this.name;
    }

    public int getHp() {
        return hp;
    }

    public void takeDamage(int hp) {
        this.hp -= hp;
        if (this.hp < 1) {
            this.isDead = true;
        }
    }

    public int getAttack() {
        return attack;
    }

    public int getInitiative() {
        return initiative;
    }
    public boolean isDead() {
        return isDead;
    }

    public int getDodgeChance() {
        return dodgeChance;
    }

    @Override
    public String toString() {
        return "Pokemon{" +
                "name='" + name + '\'' +
                ", hp=" + hp +
                ", attack=" + attack +
                ", initiative=" + initiative +
                ", dodgeChance=" + dodgeChance +
                ", isDead=" + isDead +
                '}';
    }
}
