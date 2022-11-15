//m1 en A
//m2 en B
//m1 en D
//m2 en C

class Main{
    static void main(){
        getBI1().m1();
        getBI2().m2();
        getDI1().m1();
        getDI2().m2();
    }

    static I1 getBI1(){
        return new B();
    }

    static I2 getBI2(){
        return new B();
    }

    static I1 getDI1(){
        return new D();
    }

    static I2 getDI2(){
        return new D();
    }

}

interface I1{
    void m1();
}

interface I2{
    void m2();
}
class A{
    void m1(){
        System.printSln("m1 en A");
    }
}

class B extends A implements I1, I2{
    void m2(){
        System.printSln("m2 en B");
    }
}

class C{
    void m2(){
        System.printSln("m2 en C");
    }
}

class D extends C implements I1, I2{
    void m1(){
        System.printSln("m1 en D");
    }
}