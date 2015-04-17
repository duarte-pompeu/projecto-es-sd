# Bubble Docs SD-ID
## Projecto de Sistemas Distribuídos ##

## Primeira entrega ##

Grupo de SD 33

Diogo Bastos	65675 diogo.bastos@tecnico.ulisboa.pt 

Tiago Nascimento 70493	ti_tyago@hotmail.com

Marcos Pires 	 72850	marcos.danix@gmail.com



Repositório:
[tecnico-softeng-distsys-2015/A_31_33_44-project](https://github.com/tecnico-softeng-distsys-2015/A_31_33_44-project/)


-------------------------------------------------------------------------------

## Serviço SD-ID

### Instruções de instalação 
*(Como colocar o projecto a funcionar numa máquina do laboratório)*

[0] Inicie um sistema operativo GNU/Linux na diretoria home

[1] Iniciar servidores de apoio

JUDDI: (assumindo que não está instalado)

   curl -O http://disciplinas.tecnico.ulisboa.pt/leic-sod/2014-2015/download/juddi-3.2.1_tomcat-7.0.57_port-8081.zip
   unzip juddi-3.2.1_tomcat-7.0.57_port-8081.zip
   mv juddi-3.2.1_tomcat-7.0.57_port-8081 juddi-3.2.1 #encurtar o nome
   export CATALINA_HOME=~/juddi-3.2.1
   export PATH=$PATH:$CATALINA_HOME/bin
   chmod +x $CATALINA_HOME/bin/*.sh
   startup.sh  #pode ser necessário fazer sudo startup.sh`

[2] Criar pasta temporária

   mkdir git-temp
   cd git-temp

[3] Obter versão entregue

   git clone --depth 10 -b SD_ID_R1 https://github.com/tecnico-softeng-distsys-2015/A_31_33_44-project.git

[4] Construir e executar **servidor**

   cd A_31_33_44-project/sd-id
   mvn clean package 
   mvn exec:java

[5] Construir o **cliente**
   
   cd A_31_33_44-project/sd-id-cli 
   mvn clean package -DskipTests



-------------------------------------------------------------------------------

### Instruções de teste: ###
*(Como verificar que todas as funcionalidades estão a funcionar correctamente)*


[1] Executar **cliente de testes** ...


## Testes de unidade do servidor

> cd A_31_33_44/sd-id
> mvn test

## Testes do cliente

#1 - Executar o servidor - esperar até ser imprimido "Press enter to shutdown"
> cd A_31_33_44-project/sd-id
> mvn exec:java

#2 - Executar os testes de cliente (noutra janela)

> cd A_31_33_44-project/sd-id-cli
> mvn test

[2] Executar ...



...


-------------------------------------------------------------------------------
**FIM**
