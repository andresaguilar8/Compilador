//[Error:MiInterfaz5|11]

interface MiInterfaz1 extends MiInterfaz2, MiInterfaz3{}

interface MiInterfaz2 extends MiInterfaz3{}

interface MiInterfaz3{}

interface MiInterfaz5 extends MiInterfaz6, MiInterfaz1, MiInterfaz2{}

interface MiInterfaz6 extends MiInterfaz5, MiInterfaz1, MiInterfaz2, MiInterfaz3{}