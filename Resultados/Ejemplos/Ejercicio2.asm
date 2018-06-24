TITLE imprime un ciclo

.MODEL SMALL
.STACK 64

.DATA
msgc: db 10,13, " Cuantas veces quieres imprimir un hola mundo: ",'$'

msg1: db 10,13, " hola mundo!",'$'
n db ?
i db 1

.CODE
    inicio PROC FAR
        MOV AX, @DATA
        MOV DS,AX

        MOV AH, 09H
        LEA DX,msgc
        int 21h

        ;leer caracter numérico
        MOV AH,01h
        int 21h
        sub al, 30h
        mov n,al

        a10:
            mov cl, n
            cmp cl,i
            jae a20
            jmp a30

        a20:
            mov ah, 09h
            lea dx, msg1
            int 21h
            mov cl,i
            add cl,1
            mov i,cl
            jmp a10

        a30:
            ;salida al MS-DOS
            mov ah,4ch
            int 21h

    inicio ENDP

END