///[Error:getB|9]
// getB invocado sin parametros
class A extends B {

    private B b;

    int g(int x) {
        b = new B();
        if (this.getB() == null);
    }

}

class B extends C{

    private int aa;

    B getB(int x) {
        return new B();
    }

    static void main() {}
}

class C{}
