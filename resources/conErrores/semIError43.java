//[Error:Inter9|11]

interface Inter1 extends Inter2, Inter3{}

interface Inter2 extends Inter3{}

interface Inter3{}

interface Inter7 extends Inter8, Inter1{}

interface Inter8 extends Inter9, Inter2{}

interface Inter9 extends Inter8, Inter3{}
