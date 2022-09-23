///[Error:metodo|19]

//test metodo redefinido con distintos parametros

class A {

    public int x;

    void metodo() {

    }

}

class B extends A {

    public char y;

    void metodo(int x) {

    }

    static void main () {

    }

}