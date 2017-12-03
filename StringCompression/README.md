String Compressor Task
======================

Description:
------------
A string `"QQQFAAABEEEDFFC"` can be compressed as below:

```
3QF3AB2ED2FC
```
***

Requirements:
------------

1. Implement a function to compress an input string as below:

        def compressStr(input:String, minOccurency:Int):String= ???

    The function will compress a string for some characters, 
    which are not less than 'minOccurency' in serial as a substring in the string.

    For example:  

            compressStr("ABBCDD", 2) == "A2BC2D"
            compressStr("ABBBCCDDD", 3) == "A3BCC3D"
            compressStr("ABBBCCDDD", 4) == "ABBBCCDDD"

2. Provide some unit tests if possible

