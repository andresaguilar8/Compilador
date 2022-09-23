///[Error:B|17]
//la clase B no implementa todos los metodos de Z (falta implementar metodoz)
interface X {
    int metodox();
}
interface Y{
    int metodoy();
}
interface Z extends X,Y {
    int metodoz();
}
class A {
    int metodox(){}
    int metodoy(){}
}

class B extends A implements Z {

    static void main(){}
}