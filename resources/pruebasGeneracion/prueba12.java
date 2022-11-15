//5
//5
//5

class A{
    static void main(){
        var a = new A();
        var b = 5;
        a.debugPrint(b);
        a = new A();
        a.debugPrint(b);
        var c = 5;
        a.debugPrint(c);
    }

    void m1(int i){
        System.printIln(i);
    }
}