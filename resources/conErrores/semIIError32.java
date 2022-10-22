///[Error:=|9]


class A {
    public A a;
    public B b;
    void m() {
        a = new B();
        b = new A();
    }

    static void main() {}
}

class B extends A implements X {}
interface X extends Y {}
interface Y {}