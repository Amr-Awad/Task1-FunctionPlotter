public class CharType extends TypesCombination {


    protected CharType(char b,boolean isNegative) {
        super(b);
        character = true;
        this.isNegative = isNegative;
    }

    protected double getValue() {
        return b;
    }

}
