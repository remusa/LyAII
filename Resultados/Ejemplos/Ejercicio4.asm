TITLE compara dos números
.MODEL SMALL ; compara dos valores y realiza suma o resta según los valores de entrada
.STACK 64

.data
    titulo db 'Ejemplo Codigo',10,13,'$'
    resta1 db 'El valor de la resta es:',10,13,'$'
    suma1 db 'El valor de la suma es:',10,13,'$'
    a db 2
    b db 5
    z db ?
    q db ?

    .code
        mov ax, @data
        mov ds,ax
        inicio:
        mov ah,09h
        lea dx,titulo
        int 21h
        ;compara los valores de entrada
        mov cl,a
        cmp cl,b
        jae a10
        jmp a20

    a10:
        mov al,a
        sub al,b
        mov q,al
        mov ah,09h
        lea dx,resta1
        int 21h
        mov dl,q
        add dl,30h
        mov ah,02h
        int 21h
        jmp a30

    a20:
        mov al,a
        add al,b
        mov z,al
        mov ah,09h
        lea dx,suma1
        int 21h
        mov dl,z
        add dl,30h
        mov ah,02h
        int 21h
    a30:

.exit
end
