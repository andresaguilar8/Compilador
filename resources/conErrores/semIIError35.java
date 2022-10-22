///[Error:x|9]
//x es un atributo heredado pero es privado

class X extends Y {

    private Y y;

    int m() {
        return (this.y.getX() + 99) + y.x;
    }

    static void main(){}
}

class Y {

    private int x;

    int getX(){

    }

}