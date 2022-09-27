//[Error:m2|13]

interface MiInterfaz1 extends MiInterfaz2{
    MiClase m1();
}

interface MiInterfaz2{
    void m2();
}

class MiClase implements MiInterfaz1{
    MiClase m1(){}
    int m2(){}
}