//[Error:m2|12]

class MiClase{
    int m1(char a, String b){}
    void m2(boolean c, MiClase d){}
    MiClase m2(){}
}

class MiClase2 extends MiClase{
    char m1(){}
    void m2(boolean e, MiClase f){}
    int m2(){}
}