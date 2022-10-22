///[Error:*|9]
//operador * entre char-int

class X extends Y {

    private Y y;

    int m() {
        return (this.y.getX() + 99) * y.x;
    }

    static void main(){}
}

class Y {

    public char x;

    int getX(){

    }

}