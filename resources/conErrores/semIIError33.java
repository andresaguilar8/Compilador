///[Error:return|9]

class A {
    public X a;
    public Y b;

    Y m() {
        a = new B();
        return a;
    }

    static void main() {}
}

class B implements Y {}
interface X {}
interface Y extends X {}