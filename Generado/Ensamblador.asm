org 100h
include 'emu8086.inc'
DEFINE_PRINT_STRING
DEFINE_PRINT_NUM
DEFINE_SCAN_NUM
DEFINE_PRINT_NUM_UNS
TITLE AUTOMATAS
.MODEL SMALL
.STACK 64M
.DATA

a dw 0
b dw 0
x dw 0
READ dw 0
T1 dw 0
T2 dw 0
T3 dw 0
T4 dw 0
.CODE
inicio PROC FAR
MOV AX,@data
MOV DS,AX

MOV AX,b
MUL a 
MOV T1,AX

MOV AX,a
ADD AX,T1
MOV T2,AX

MOV AX,b
DIV a
MOV T3,AX

MOV AX,T2
SUB AX,T3
MOV T4,AX

MOV AX,T4
MOV x,AX

MOV AH,4CH
INT 21h
inicio ENDP
END
ret

