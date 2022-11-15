//1
//2
//4
//8
//16
//32
//64
//128
//256
//512
//1024

class A{
    static void main(){
        var a = 1;
        while(a <= 1024) {
            var b = a;
            A.print(b);
            a = A.duplicar(b);
            A.duplicar(b);
        }
    }

    static void print(int i){
        var v1 = 5;
        var v2 = 7;
        System.printIln(i);
    }

    static int duplicar(int i){
        return i * 2;
    }
}