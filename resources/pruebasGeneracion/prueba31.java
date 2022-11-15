//m1 en A
//m1 en B
//m2 en B
//m2 en C
//m1 en D
//m2 en D
//m1 en B
//m1 en B
//m2 en B
//m2 en D
//m1 en D
//m2 en D

class Main{
    static void main(){
        new A().m1();
        new B().m1();
        new B().m2();
        new C().m2();
        new D().m1();
        new D().m2();
        getBAsA().m1();
        getBAsI1().m1();
        getBAsI2().m2();
        getDAsC().m2();
        getDAsI1().m1();
        getDAsI2().m2();
    }

    static A getBAsA(){
        return new B();
    }

    static I1 getBAsI1(){
        return new B();
    }

    static I2 getBAsI2(){
        return new B();
    }

    static C getDAsC(){
        return new D();
    }

    static I1 getDAsI1(){
        return new D();
    }

    static I2 getDAsI2(){
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
    void m1(){
        System.printSln("m1 en B");
    }
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
    void m2(){
        System.printSln("m2 en D");
    }
}