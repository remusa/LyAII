org 100h
include "emu8086.inc"
DEFINE_PRINT_STRING
DEFINE_PRINT_NUM
DEFINE_PRINT_NUM_UNS
DEFINE_SCAN_NUM
 TITLE Operaciones
.MODEL small        ;el programa va ser pequeño
.STACK 64
;-----------------Declaración de variables-----------------------
.DATA
saltoLN db 0D,0AH,"$"
msg1    DB 10,13,"Ingresa el valor de a ",'$'
msg3    DB 10,13,"Ingresa el valor de b ",'$'
msg4    DB 10,13,"la suma es:  ",'$'
a dw ?
b dw ?
c dw ?
;-------------------------------------------------------------------------------------
.CODE
mov ah,09h
lea dx,msg1
int 21h
call SCAN_NUM
mov a,cx
lea dx, saltoLN
mov ah,09h
int 21h
 mov ah,09h
lea dx,msg3
int 21h
call SCAN_NUM
mov b,cx
lea dx, saltoLN
mov ah,09h
int 21h
mov ax,a
mov cx,b
add ax,cx
mov c, ax
mov ah,09h
lea dx,msg4
int 21h
mov ax,c
call PRINT_NUM
lea dx,saltoLN
mov ah,09h
int 21h
;mov ax,@data
;mov ds,ax
;mov ax,13
;mov a,ax
.exit
