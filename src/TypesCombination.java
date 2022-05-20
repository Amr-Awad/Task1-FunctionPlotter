public abstract class TypesCombination {
    protected double a ;
    protected char b ;

    public boolean isNegative() {
        return isNegative;
    }

    public boolean isNegative;
    public boolean character;

    protected TypesCombination(double a)
    {
        this.a = a;
    }

    protected TypesCombination(char b)
    {
        this.b = b;

    }


    protected abstract double getValue() ;

    public boolean isCharacter() {
        return character;
    }
}


