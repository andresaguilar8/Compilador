///[Error:C|6]

//herencia circular, el error podria ser cualquiera de las interfaces

class A extends B implements C{
}

interface C extends Y{}
interface Y extends H{}
interface H extends Z{}
interface Z extends X{}
interface X extends C{}

class B implements C, H{
    static void main(){}
}
