///[Error:Z|19]
//test de una una clase  que implementa una interface que extiende a otras dos interfaces


interface X {

    void metodox();
}

interface Y {
    void metodoy();
}

interface Z extends X, Y{

}


class A implements Z {


    static void main() {}
}