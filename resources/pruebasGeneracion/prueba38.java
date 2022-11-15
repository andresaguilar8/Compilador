class A {

    private String x;

    static void main () {
        var objA = new A();
        objA.setX("hola");
        objA.printX();


        var objB = new B();
        objB.setX("chau");
        objB.mI1();
        objB.mI2();
    }

    void printX() {
        System.printSln(this.x);
    }

    void setX(String x) {
        this.x = x;
        return;
    }

    void mI1() {
        System.printSln("m1 en A");
    }

    void mI2() {
        System.printSln("m2 en A");
    }
}

class B extends A implements I {

    void mI2() {
        System.printSln("m2 en B");
    }

    void setX(String x) {
        System.printSln("no puedo cambiar el valor de x");
    }

}

interface I {

    void mI1();
    void mI2();

}