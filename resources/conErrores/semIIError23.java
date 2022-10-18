///[Error:return|9]
// metodo tiene sentencia con una expresion de tipo boolean y el metodo retorna int
class A extends B {

    private B b;

    int g(int x) {
        b = new B();
        return (this.getB() == null);
    }

}

class B extends C{

    private int aa;

    B getB() {
        return new B();
    }

    static void main() {}
}

class C{}
