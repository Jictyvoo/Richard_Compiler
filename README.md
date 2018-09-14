# Richard Compiler

A compiler for Richard Language written in Java for EXA 817 discipline

## Team

* Eduardo Marques
* João Victor Olveira Couto

## Grammar

Here is the grammar for the language, with all symbols accepted by the language

### Reserved Words

* class
* const
* variables
* method
* return
* main
* if
* then
* else
* while
* read
* write
* void
* int
* float
* bool
* string
* true
* false
* extends

### Identifier

char(char | digit | _)*

### Number

(-)?(" ")*digit(digit*(.digit(digit)*)?)

### Digit

[0-9]

### Character

[a-z] | [A-Z]

### Arithmetic Operators

+ | - | * | / | ++ | --

### Relational Operators

!= | == | < | <= | > | >= | =

### Logic Operators

! | && | ||

### Comments

Will exists two comment types, line comment and block comment

* Line Comment - //comment
* Block Comment - /\*comment\*/

### Delimiters

; | , | ( | ) | [ | ] | { | } | .

### Strings

"(char | digit | symbol | \\")*"

### Symbols

Only will be accepted symbols between ASCII 32 and ASCII 126 (except for ASCII 34 symbol)

### Space

Only will be accepted like space symbol ASCII 9 and ASCII 32
