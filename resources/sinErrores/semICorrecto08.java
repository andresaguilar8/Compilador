//prueba de consolidación clases (metodos heredados/redefinidos)

class MiClase{
    int m1(char a, String b){}
    void m2(boolean c, MiClase d){}
    MiClase m2(){}
    String m4(){}
    static void m4(char g, String h){}
}

class MiClase2 extends MiClase{
    char m1(){}
    void m2(boolean e, MiClase f){}
    String m4(){}
    static void m4(char i, String j){}

    static void main(){}
}