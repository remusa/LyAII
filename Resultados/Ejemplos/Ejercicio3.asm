TITLE compara dos números

.MODEL SMALL ;el programa va a ser pequeño
.STACK 64
;--------------Declaración de variables-----------------
.DATA
    n1 DB 0     ;DB dato de 8bits inicializado en cero
    msgn1 DB 10,13,"Dame tu calificación","$"
    msgn2 DB 10,13,"Aprobaste la materia","$"
    msgn3 DB 10,13,"Reprobaste la metaria","$"
;-------------------------------------------------------
.CODE
    inicio PROC FAR
        MOV AX,@data
        MOV DS,AX
;--------------Mostrar mensaje y habilitar teclado------
        MOV AH,09H  ;función 09h de int 21h para mostrar mensaje
        LEA DX,msgn1
        INT 21h
        MOV AH,01h  ;habilita teclado para ingresar número
        INT 21h
        SUB AL,30h  ;se resta 30 para convertirlo en número
        MOV n1,AL
;--------------Mostrar resultados-----------------------
        MOV CL,n1
        CMP CL,7
        JAE a10
        JMP a20

        a10:
            MOV AH,09h
            LEA DX,msgn2
            INT 21h
            JMP a30

        a20:
            MOV AH,09h
            LEA DX,msgn3
            INT 21h

        a30:
            MOV AH,4ch  ;devuelve control para seguir trabajando en consola
            INT 21h
            inicio ENDP

END