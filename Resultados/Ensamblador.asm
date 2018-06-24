org 100h
include 'emu8086.inc'

DEFINE_PRINT_STRING
DEFINE_PRINT_NUM
DEFINE_SCAN_NUM
DEFINE_PRINT_NUM_UNS

TITLE Codigo Ensamblador
.MODEL SMALL
.STACK 64M

.DATA
colegiatura dw ?
nMaterias dw ?
costoMateria dw ?
promedio dw ?
msj1 db 10,13,"materias : "  ,'$'
msj2 db 10,13,"costo materia : "  ,'$'
msj3 db 10,13,"promedio : "  ,'$'
T1 dw ?
T2 dw ?
T3 dw ?
T4 dw ?
T5 dw ?
msj4 db 10,13,"colegiatura : "  ,'$'

.CODE
inicio PROC FAR
MOV AX,@data
MOV DS,AX

MOV AH,09h
LEA DX,msj1
INT 21h

CALL SCAN_NUM
MOV nMaterias,CX

MOV AH,09h
LEA DX,msj2
INT 21h

CALL SCAN_NUM
MOV costoMateria,CX

MOV AH,09h
LEA DX,msj3
INT 21h

CALL SCAN_NUM
MOV promedio,CX

MOV AX,costoMateria
MOV BX,nMaterias
MUL BX 
MOV T1,AX

MOV AX,T1
MOV colegiatura,AX

A100:

MOV CX,promedio
CMP CX,90
JBE A10
JMP A20

A20:

A10:

MOV AX,colegiatura
MOV BX,7
MUL BX 
MOV T2,AX

MOV AX,T2
MOV colegiatura,AX

A30:

JA A20

MOV CX,promedio
CMP CX,80
JBE A70
JMP A50

A50:

A70:

MOV CX,promedio
CMP CX,90
JB A40
JMP A50

JA A50

A40:

MOV AX,colegiatura
MOV BX,9
MUL BX 
MOV T3,AX

MOV AX,T3
MOV colegiatura,AX

A60:

JA A50

MOV CX,promedio
CMP CX,80
JB A80
JMP A90

A90:

A80:

MOV AX,colegiatura
MOV BX,11
MUL BX 
MOV T4,AX

MOV AX,T4
MOV colegiatura,AX

A110:

JA A90

MOV AX,colegiatura
XOR DX,DX
MOV BX,10
DIV BX
MOV T5,AX

MOV AX,T5
MOV colegiatura,AX

MOV AH,09h
LEA DX,msj4
INT 21h

MOV AX,colegiatura
CALL PRINT_NUM

MOV AH,4CH
INT 21h
inicio ENDP

END
ret

