//prueba de consolidación clases (interfaces implementadas)

interface MiInterfaz1 extends MiInterfaz2, MiInterfaz3{
    int m1();
}

interface MiInterfaz2 extends MiInterfaz3{
    void m2();
}

interface MiInterfaz3{
    String m3();
}

class MiClase implements MiInterfaz1, MiInterfaz2, MiInterfaz3{
    int m1(){}
    void m2(){}
    String m3(){}
    static void main(){}
}