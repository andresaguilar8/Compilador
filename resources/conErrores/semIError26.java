///[Error:x|21]

//test nombre de atributo heredado repetido

class A {

    public int x;

}

class B extends A {




    static void main () {}
}

class C extends B {

    public String x;

}