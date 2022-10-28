//[Error:return|8]

class A {

    public B b;

    V met() {
        return b;
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
interface X {}
interface Y extends E, F{}