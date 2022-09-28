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

    int metodoz(){}
    static void main(){}
}