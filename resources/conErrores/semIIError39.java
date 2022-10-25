///[Error:=|6]

class A {
    public X x;
    void metodo() {
        this.x = new B();
    }
    static void main(){}
}

class B implements Z{

}

interface X extends Z {}
interface Y {}
interface Z {}