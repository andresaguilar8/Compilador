//56789
//54321
//012345
//-10123456789
//
//-1012345

class A{
    static void main(){
        A.print(5);
        A.print(-1);
    }

    static void print(int i){
        var a = i;
        while(a < 10) {
            System.printI(a);
            a += 1;
        }
        System.println();
        a = i;
        while(a > 0) {
            System.printI(a);
            a -= 1;
        }
        System.println();
        while(a <= 5){
            System.printI(a);
            a = a + 1;
        }
        System.println();
    }
}