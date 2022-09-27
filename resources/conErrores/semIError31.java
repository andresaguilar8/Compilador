//[Error:MiInterfaz1|11]

interface MiInterfaz1 extends MiInterfaz2{
    int m1();
}

interface MiInterfaz2{
    void m2();
}

class MiClase implements MiInterfaz1{
    int m1(){}
}