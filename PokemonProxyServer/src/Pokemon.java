public class Pokemon implements IPokemon{
    String name;
    int hp;
    int attack;
    int defense;
    int initiative;

    public Pokemon(String name, int hp, int attack, int defense, int initiative) {
        this.name = name;
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.initiative = initiative;
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

    public int getDefense() {
        return defense;
    }

    public int getInitiative() {
        return initiative;
    }
}
