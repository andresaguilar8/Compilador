///[Error:met|13]

//test nombre de metodo repetido dentro de una misma clase

class A {

    public int x;

    void met() {

    }

    int met() {

    }

}

class B extends A {

    public char y;
    static void main() {}
}