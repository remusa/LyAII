TITLE Operaciones de Suma y resta
.MODEL SMALL        ;el programa pide dos valores los suma y los resta
.STACK 64

;-----------------Declaración de variables-----------------------
.DATA
n1      DB 0        ;DB dato de 8bits inicializado en cero
n2      DB 0
suma    DB 0        ;donde se va a guardar el resultado
resta   DB 0
msgn1   DB 10,13,"Dame el primer valor:",'$'
msgn2   DB 10,13,"Dame el segundo valor:",'$'
msg3    DB 10,13,"Suma = ",'$'
msg4    DB 10,13,"Resta = ",'$'

;-------------------------------------------------------------------------------------
.CODE
    inicio PROC FAR
    MOV AX,@data        ;se direcciona al segmento de datos
    MOV DS,AX
;-----------------Mostrar mensaje y habilitar teclado--------------------------------

    MOV AH,09h          ;función 09h de la interrupción 21h para mostrar msgn1
    LEA DX,msgn1
    INT 21h
    MOV AH,01h          ;habilita teclado para ingresar número
    INT 21h
    SUB AL,30h          ;se resta 30 para convertirlo a número
    MOV n1,AL
;----------------Mostrar mensaje y habilitar teclado----------------------------

    MOV AH,09h
    LEA DX,msgn2
    INT 21h
    MOV AH,01h
    INT 21h
    SUB AL,30h
    MOV n2,AL
;----------------Realiza operaciones---------------------------------------------

    MOV AL,n1
    ADD Al,n2
    MOV suma,AL

    MOV AL,n1
    SUB AL,n2
    MOV resta,AL
;-------------------Mostrar Resultados-----------------------------------------

    MOV AH,09h
    LEA DX,msg3
    INT 21h
    MOV DL,suma
    ADD DL,30h      ;ahora se suma para visualizar
    MOV AH,02       ;para mostrar mensaje en pantalla
    INT 21h

    MOV AH,09h
    LEA DX,msg4
    INT 21h
    MOV DL,resta
    ADD DL,30h
    MOV AH,02
    INT 21h
;---------------------------------------------------------------------------

    MOV AH,4ch      ;devuelve el control para seguir trabajando en consola
    INT 21h

    inicio ENDP
END
