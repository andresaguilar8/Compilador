//[Error:MiInterfaz8|9]

interface MiInterfaz1 extends MiInterfaz2, MiInterfaz3{}

interface MiInterfaz2 extends MiInterfaz3{}

interface MiInterfaz3{}

interface MiInterfaz7 extends MiInterfaz8, MiInterfaz1{}

interface MiInterfaz8 extends MiInterfaz9, MiInterfaz2{}

interface MiInterfaz9 extends MiInterfaz8, MiInterfaz3{}