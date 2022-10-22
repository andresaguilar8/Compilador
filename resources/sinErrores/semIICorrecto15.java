
class A {
    public X a;

    X m() {
        a = new B();
        return this.m2();
    }

    B m2() {

    }
    static void main() {}
}

class B extends A implements X {}
interface X {}