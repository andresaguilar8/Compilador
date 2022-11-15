//m1 en A
//m1 en A
//m2 en A
//m1 en B
//m1 en B
//m2 en A

class Main{
    static void main(){
        getAAsI1().m1();
        getAAsI2().m1();
        getAAsI2().m2();
        getBAsI1().m1();
        getBAsI2().m1();
        getBAsI2().m2();
    }

    static I1 getAAsI1(){
        return new A();
    }

    static I2 getAAsI2(){
        return new A();
    }

    static I1 getBAsI1(){
        return new B();
    }

    static I2 getBAsI2(){
        return new B();
    }
}

interface I1{
    void m1();
}

interface I2 extends I1{
    void m2();
}

class A implements I1, I2{
    void m1(){
        System.printSln("m1 en A");
    }

    void m2(){
        System.printSln("m2 en A");
    }
}

class B extends A implements I1{

    void m1(){
        System.printSln("m1 en B");
    }
}