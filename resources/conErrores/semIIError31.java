///[Error:return|9]


class B {
    public A a;
    public B b;
    void m() {
        if (a != new A()) {
            return this.a;
        }
    }

    static void main() {}
}

class A implements X {}
interface X extends Y {}
interface Y {}