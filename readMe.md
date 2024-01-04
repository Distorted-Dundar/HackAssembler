# Assembler for Hack Assembly

[![LinkedIn][linkedin-shield]][linkedin-url]

- [PROJECT SPECS](https://www.nand2tetris.org/project06)

<!-- ABOUT THE PROJECT -->
## About The Project

An Assembler (A program that translates Assembly programs to binary executable).
More specifically this is a project to translate hack assembly (Nand2Tetris assembly)
that is executed on a custom-made virtual Computer (The hack Computer). 
See [N2T part 1](https://www.nand2tetris.org)

### Built With

* Java
* Assembler (Provided Nand2Tetris Assembler for debugging)
* CPU emulator (Provided Nand2Tetris CPU emulator for running assembly programs)

### Educational goals
- Applying Dependency Inversion Principle
- Applying Interface Separation Principle
- Reducing complicated code into functions (applying Clean Code advice)
- Making Code readable

<!-- GETTING STARTED -->
## Getting Started

You first need to assert that the hack folder is outside the src code. 
You can debug what directory java is on by doing the following print statement

``` java
System.out.println(System.getProperty("user.dir"));
```

### Prerequisites

knowledge of editing configurations is crucial.
1. Edit the configurations of **HackAssembler.java**
2. The program arguments should be somewhere along the lines of **path\*.asm**
3. When running the program you will find the Assembled hack code within that same directory.
4. Use the CPU Emulator to run the program or use the Assembler as proof that the hack code is correct


<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->

[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://www.linkedin.com/in/bflo/
