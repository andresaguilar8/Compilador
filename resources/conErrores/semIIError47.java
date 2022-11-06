///[Error:return|19]

class A {

    public D d;

    //esto tiene que andar
    V met() {
        return d;
    }

    //esto tambien
    H metodo() {
        return d;
    }

    //esto no
    X metodo_() {
        return new C();
    }
    static void main(){}
}


class B implements G, H{}
class C extends B implements V, W{}
class D extends C implements X, Y{}

interface E {}
interface F {}
interface G {}
interface H {}
interface V {}
interface W {}
interface X extends V{}
interface Y extends E, F{}