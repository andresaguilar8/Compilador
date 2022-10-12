///[Error:+|15]

//suma entre tipo int y boolean
class B {
    public boolean x;
    public int z;
}

class A extends B {

    public int y;

    void met() {
        var x = this.y + 3 + this.z;
        var i = y().x + 3;
    }

    B y() {
    }
}