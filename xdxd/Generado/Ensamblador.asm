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
.CODE
inicio PROC FAR
MOV AX,@data
MOV DS,AX

MOV AX,9
MOV a,AX

MOV AH,4CH
INT 21h
inicio ENDP
END
ret

