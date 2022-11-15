//5
//5
//5

class A{
    static void main(){
        var a = new A();
        var b = 5;
        a.m1(b);
        a = new A();
        a.m1(b);
        var c = 5;
        a.m1(c);
    }

    void m1(int i){
        System.printIln(i);
    }
}