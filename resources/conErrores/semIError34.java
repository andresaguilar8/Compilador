///[Error:A|4]

//test herencia circular
class A extends C implements X, Y{
    static void main(){}
}

class B extends A {

}

interface X {}
interface Y extends X{}

class C extends A{}