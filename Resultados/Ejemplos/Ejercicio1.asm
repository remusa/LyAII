TITLE imprime un letrero

.MODEL SMALL
.STACK 64

.DATA
msg: db "Hola, mundo! :)", 0Dh, 0Ah, 24h
msg2: db "Adios, mundo! :(", 0Dh, 0Ah, 24h

.CODE
    inicio PROC FAR
        MOV AX,@data
        MOV DS,AX
        MOV AH,09H
        LEA DX,msg
        INT 21h
        MOV AH,4ch
        INT 21h
        MOV ah,4ch
        INT 21h
    inicio ENDP
END