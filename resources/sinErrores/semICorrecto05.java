//prueba de consolidación de interfaces

interface MiInterfaz1 extends MiInterfaz2, MiInterfaz3{
    int metodoDeLaInterfaz1();
}

interface MiInterfaz2{
    char metodoDeLaInterfaz2();
}

interface MiInterfaz3 extends MiInterfaz4, MiInterfaz5{
    boolean metodoDeLaInterfaz3();
}

interface MiInterfaz4 extends MiInterfaz5{
    String metodoDeLaInterfaz4();
}

interface MiInterfaz5{
    MiInterfaz1 metodoDeLaInterfaz5();
}

class MiClase{
    static void main(){}
}