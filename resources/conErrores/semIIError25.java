///[Error:=|5]
//var x = null --> no se puede inferir el tipo
class C {
    char metodostatic() {
        var x = null;
        return 'a';
    }
}

class A {
    public int a1;

    void met() {
    }

    static void main() {
    }
}